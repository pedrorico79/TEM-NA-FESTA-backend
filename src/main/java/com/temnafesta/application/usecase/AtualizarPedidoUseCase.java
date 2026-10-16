package com.temnafesta.application.usecase;

import com.temnafesta.application.dto.AtualizarPedidoCommand;
import com.temnafesta.domain.exception.RegraDeNegocioException;
import com.temnafesta.domain.model.ItemPedido;
import com.temnafesta.domain.model.Pedido;
import com.temnafesta.domain.model.Produto;
import com.temnafesta.domain.ports.repository.PedidoRepositoryPort;
import com.temnafesta.domain.ports.repository.ProdutoRepositoryPort;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

public class AtualizarPedidoUseCase {

    private final PedidoRepositoryPort pedidoRepositoryPort;
    private final ProdutoRepositoryPort produtoRepositoryPort;

    public AtualizarPedidoUseCase(PedidoRepositoryPort pedidoRepositoryPort, ProdutoRepositoryPort produtoRepositoryPort) {
        this.pedidoRepositoryPort = pedidoRepositoryPort;
        this.produtoRepositoryPort = produtoRepositoryPort;
    }

    @Transactional
    public Pedido executar(AtualizarPedidoCommand command) {
        Pedido pedido = pedidoRepositoryPort.buscarPorId(command.pedidoId())
                .orElseThrow(() -> new RegraDeNegocioException("Pedido não encontrado com o ID: " + command.pedidoId()));

        pedido.atualizarDadosBasicos(
                command.dataEntrega(),
                command.taxaEntrega(),
                command.observacao(),
                command.enderecoEntrega()
        );

        if (command.eventoId() != null) {
            pedido.atualizarEventoId(command.eventoId());
        }

        List<ItemPedido> novosItens = new ArrayList<>();
        for (AtualizarPedidoCommand.ItemCommand itemDto : command.itens()) {
            Produto produto = produtoRepositoryPort.buscarPorId(itemDto.produtoId())
                    .orElseThrow(() -> new RegraDeNegocioException("Produto não encontrado com o ID: " + itemDto.produtoId()));

            novosItens.add(new ItemPedido(
                    null,
                    produto.getId(),
                    itemDto.quantidade(),
                    itemDto.precoUnitario(),
                    itemDto.observacaoItem()
            ));
        }

        pedido.substituirItens(novosItens);

        return pedidoRepositoryPort.atualizar(pedido);
    }
}