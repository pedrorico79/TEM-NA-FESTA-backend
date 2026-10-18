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
import java.util.Set;
import java.util.stream.Collectors;

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

        pedido.atualizarEventoId(command.eventoId());

        List<ItemPedido> novosItens = new ArrayList<>();
        Set<Long> produtoIdsAtuais = pedido.getItens().stream()
                .map(ItemPedido::getProdutoId)
                .collect(Collectors.toSet());

        for (AtualizarPedidoCommand.ItemCommand itemDto : command.itens()) {
            ItemPedido itemExistente = pedido.getItens().stream()
                    .filter(item -> item.getProdutoId().equals(itemDto.produtoId()))
                    .findFirst()
                    .orElse(null);

            // Um produto removido do catálogo pode continuar associado a um pedido antigo.
            Produto produto = produtoRepositoryPort.buscarPorId(itemDto.produtoId())
                    .orElse(itemExistente != null ? itemExistente.getProduto() : null);
            if (produto == null) {
                throw new RegraDeNegocioException("Produto não encontrado com o ID: " + itemDto.produtoId());
            }

            if (!produto.isAtivo() && !produtoIdsAtuais.contains(produto.getId())) {
                throw new RegraDeNegocioException(
                        "Produto inativo não pode ser adicionado ao pedido. ID: " + itemDto.produtoId());
            }

            ItemPedido novoItem = new ItemPedido(
                    null,
                    produto.getId(),
                    itemDto.quantidade(),
                    itemDto.precoUnitario(),
                    itemDto.observacaoItem()
            );
            novoItem.setProduto(produto);
            novosItens.add(novoItem);
        }

        pedido.substituirItens(novosItens);

        if (pedido.calcularTotalComprometido().compareTo(pedido.getValorTotal()) > 0) {
            throw new RegraDeNegocioException(
                    "O novo total do pedido não pode ser menor que os pagamentos confirmados ou pendentes.");
        }

        return pedidoRepositoryPort.atualizar(pedido);
    }
}
