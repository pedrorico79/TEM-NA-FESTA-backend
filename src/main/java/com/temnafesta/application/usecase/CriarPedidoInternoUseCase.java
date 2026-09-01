package com.temnafesta.application.usecase;

import com.temnafesta.application.dto.CriarPedidoCommand;
import com.temnafesta.domain.exception.RegraDeNegocioException;
import com.temnafesta.domain.model.ItemPedido;
import com.temnafesta.domain.model.Pedido;
import com.temnafesta.domain.model.Produto;
import com.temnafesta.domain.ports.repository.ClienteRepositoryPort;
import com.temnafesta.domain.ports.repository.PedidoRepositoryPort;
import com.temnafesta.domain.ports.repository.ProdutoRepositoryPort;
import com.temnafesta.domain.vo.StatusProducaoEnum;

import java.util.ArrayList;

public class CriarPedidoInternoUseCase {

    private final PedidoRepositoryPort pedidoRepositoryPort;
    private final ClienteRepositoryPort clienteRepositoryPort;
    private final ProdutoRepositoryPort produtoRepositoryPort;

    public CriarPedidoInternoUseCase(
            PedidoRepositoryPort pedidoRepositoryPort,
            ClienteRepositoryPort clienteRepositoryPort,
            ProdutoRepositoryPort produtoRepositoryPort) {

        this.pedidoRepositoryPort = pedidoRepositoryPort;
        this.clienteRepositoryPort = clienteRepositoryPort;
        this.produtoRepositoryPort = produtoRepositoryPort;
    }

    public Pedido executar(CriarPedidoCommand command) {

        // 1. Valida existência do Cliente
        clienteRepositoryPort.buscarPorId(command.clienteId())
                .orElseThrow(() ->
                        new RegraDeNegocioException(
                                "Cliente não encontrado com o ID: " + command.clienteId()
                        )
                );

        // 2. Instancia o Pedido
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
                command.enderecoEntregaId(),
                new ArrayList<>(),
                new ArrayList<>()
        );

        // 3. Monta os itens
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

            ItemPedido item = new ItemPedido(
                    null,
                    produto.getId(),
                    itemDto.quantidade(),
                    itemDto.precoUnitario(),
                    itemDto.observacaoItem()
            );

            // Mantém o produto completo no domínio
            item.setProduto(produto);

            novoPedido.adicionarItem(item);
        }

        // 4. Persiste o pedido
        return pedidoRepositoryPort.salvar(novoPedido);
    }
}