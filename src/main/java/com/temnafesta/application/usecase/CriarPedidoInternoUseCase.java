package com.temnafesta.application.usecase;

import com.temnafesta.application.dto.CriarPedidoCommand;
import com.temnafesta.domain.exception.NaoEncontradoException;
import com.temnafesta.domain.exception.RegraDeNegocioException;
import com.temnafesta.domain.model.Cliente;
import com.temnafesta.domain.model.ItemPedido;
import com.temnafesta.domain.model.Pedido;
import com.temnafesta.domain.model.Produto;
import com.temnafesta.domain.ports.out.PedidoEventPublisherPort;
import com.temnafesta.domain.ports.out.repository.ClienteRepositoryPort;
import com.temnafesta.domain.ports.out.repository.PedidoRepositoryPort;
import com.temnafesta.domain.ports.out.repository.ProdutoRepositoryPort;
import com.temnafesta.domain.vo.StatusProducaoEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.util.ArrayList;

public class CriarPedidoInternoUseCase {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(CriarPedidoInternoUseCase.class);

    private final PedidoRepositoryPort pedidoRepositoryPort;
    private final ClienteRepositoryPort clienteRepositoryPort;
    private final ProdutoRepositoryPort produtoRepositoryPort;
    private final PedidoEventPublisherPort pedidoEventPublisherPort;
    private final Clock relogioNegocio;

    public CriarPedidoInternoUseCase(
            PedidoRepositoryPort pedidoRepositoryPort,
            ClienteRepositoryPort clienteRepositoryPort,
            ProdutoRepositoryPort produtoRepositoryPort,
            PedidoEventPublisherPort pedidoEventPublisherPort) {

        this(
                pedidoRepositoryPort,
                clienteRepositoryPort,
                produtoRepositoryPort,
                pedidoEventPublisherPort,
                com.temnafesta.domain.time.RelogioNegocio.sistema()
        );
    }

    public CriarPedidoInternoUseCase(
            PedidoRepositoryPort pedidoRepositoryPort,
            ClienteRepositoryPort clienteRepositoryPort,
            ProdutoRepositoryPort produtoRepositoryPort,
            PedidoEventPublisherPort pedidoEventPublisherPort,
            Clock relogioNegocio) {

        this.pedidoRepositoryPort = pedidoRepositoryPort;
        this.clienteRepositoryPort = clienteRepositoryPort;
        this.produtoRepositoryPort = produtoRepositoryPort;
        this.pedidoEventPublisherPort = pedidoEventPublisherPort;
        this.relogioNegocio = relogioNegocio != null
                ? relogioNegocio
                : com.temnafesta.domain.time.RelogioNegocio.sistema();
    }

    @Transactional
    public Pedido executar(CriarPedidoCommand command) {

        Cliente cliente = clienteRepositoryPort.buscarPorId(command.clienteId())
                .orElseThrow(() -> new NaoEncontradoException(
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
                    .orElseThrow(() -> new NaoEncontradoException(
                            "Produto não encontrado com o ID: " + itemDto.produtoId()));

            if (!produto.isAtivo()) {
                throw new RegraDeNegocioException(
                        "Produto inativo não pode ser adicionado ao pedido. ID: "
                                + itemDto.produtoId());
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

        return pedidoRepositoryPort.salvar(novoPedido);
    }
}