package com.temnafesta.application.usecase;

import com.temnafesta.application.dto.CriarPedidoCommand;
import com.temnafesta.application.exception.RecursoNaoEncontradoException;
import com.temnafesta.domain.exception.RegraDeNegocioException;
import com.temnafesta.domain.model.Cliente;
import com.temnafesta.domain.model.ItemPedido;
import com.temnafesta.domain.model.Pedido;
import com.temnafesta.domain.model.Produto;
import com.temnafesta.domain.ports.out.PedidoEventPublisherPort;
import com.temnafesta.domain.ports.repository.ClienteRepositoryPort;
import com.temnafesta.domain.ports.repository.PedidoRepositoryPort;
import com.temnafesta.domain.ports.repository.ProdutoRepositoryPort;
import com.temnafesta.domain.vo.StatusProducaoEnum;
import org.springframework.transaction.annotation.Transactional;
import com.temnafesta.infrastructure.persistence.adapter.messaging.RabbitMQPedidoEventPublisher;
import com.temnafesta.presentation.dto.PedidoResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class CriarPedidoInternoUseCase {

    private final PedidoRepositoryPort pedidoRepositoryPort;
    private final ClienteRepositoryPort clienteRepositoryPort;
    private final ProdutoRepositoryPort produtoRepositoryPort;
    private final PedidoEventPublisherPort pedidoEventPublisherPort;

    private static final Logger LOGGER = LoggerFactory.getLogger(CriarPedidoInternoUseCase.class);


    public CriarPedidoInternoUseCase(
            PedidoRepositoryPort pedidoRepositoryPort,
            ClienteRepositoryPort clienteRepositoryPort,
            ProdutoRepositoryPort produtoRepositoryPort, PedidoEventPublisherPort pedidoEventPublisherPort) {

        this.pedidoRepositoryPort = pedidoRepositoryPort;
        this.clienteRepositoryPort = clienteRepositoryPort;
        this.produtoRepositoryPort = produtoRepositoryPort;
        this.pedidoEventPublisherPort = pedidoEventPublisherPort;
    }

    @Transactional
    public Pedido executar(CriarPedidoCommand command) {

        Cliente cliente = clienteRepositoryPort.buscarPorId(command.clienteId())
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Cliente não encontrado com o ID: " + command.clienteId()));

        if (!cliente.isAtivo()) {
            throw new RegraDeNegocioException(
                    "Cliente inativo não pode ser associado a um novo pedido. ID: "
                            + command.clienteId());
        }

        Pedido novoPedido = new Pedido(
                null,
                null,
                command.dataEntrega(),
                command.taxaEntrega(),
                command.observacao(),
                StatusProducaoEnum.RASCUNHO,
                command.clienteId(),
                command.usuarioId(),
                command.eventoId(),
                command.enderecoEntrega(),
                new ArrayList<>(),
                new ArrayList<>(),
                true,
                false
        );

        for (CriarPedidoCommand.ItemCommand itemDto : command.itens()) {

            Produto produto = produtoRepositoryPort.buscarPorId(itemDto.produtoId())
                    .orElseThrow(() ->
                            new RegraDeNegocioException(
                                    "Produto não encontrado com o ID: " + itemDto.produtoId()
                            )
                    );

            if (!produto.isAtivo()) {
                throw new RegraDeNegocioException(
                        "Produto inativo não pode ser adicionado ao pedido. ID: " + itemDto.produtoId());
            }

            ItemPedido item = ItemPedido.comAcrescimo(
                    null,
                    produto.getId(),
                    itemDto.quantidade(),
                    itemDto.precoUnitario(),
                    itemDto.acrescimo(),
                    itemDto.observacaoItem()
            );

            item.setProduto(produto);

            novoPedido.adicionarItem(item);
        }

        novoPedido.definirDescontoPedido(command.descontoPedido());

        // 4. Persiste o pedido + dispara evento
        Pedido pedidoSalvo = pedidoRepositoryPort.salvar(novoPedido);
        try {
            pedidoEventPublisherPort.publicarPedidoCriado(pedidoSalvo, cliente);
        } catch (Exception error) {
            LOGGER.error("Não foi possível publicar o evento. {}", error.getMessage());
        }
        return pedidoSalvo;
    }
}