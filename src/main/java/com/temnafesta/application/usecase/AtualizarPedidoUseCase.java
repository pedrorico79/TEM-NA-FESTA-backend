package com.temnafesta.application.usecase;

import com.temnafesta.application.dto.AtualizarPedidoCommand;
import com.temnafesta.domain.exception.NaoEncontradoException;
import com.temnafesta.domain.exception.RegraDeNegocioException;
import com.temnafesta.domain.model.ItemPedido;
import com.temnafesta.domain.model.Pedido;
import com.temnafesta.domain.model.Produto;
import com.temnafesta.domain.ports.out.repository.PedidoRepositoryPort;
import com.temnafesta.domain.ports.out.repository.ProdutoRepositoryPort;
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
                .orElseThrow(() -> new NaoEncontradoException("Pedido não encontrado com o ID: " + command.pedidoId()));

        java.math.BigDecimal descontoPedido = command.descontoPedido() != null
                ? command.descontoPedido()
                : pedido.getDescontoPedido();
        pedido.definirDescontoPedido(java.math.BigDecimal.ZERO);

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

        Set<ItemPedido> itensAtuaisUtilizados = new java.util.HashSet<>();
        for (AtualizarPedidoCommand.ItemCommand itemDto : command.itens()) {
            ItemPedido itemExistente;
            if (itemDto.itemId() != null) {
                itemExistente = pedido.getItens().stream()
                        .filter(item -> itemDto.itemId().equals(item.getId()))
                        .findFirst()
                        .orElseThrow(() -> new RegraDeNegocioException(
                                "O item " + itemDto.itemId() + " não pertence a este pedido."));
                if (!itemExistente.getProdutoId().equals(itemDto.produtoId())) {
                    throw new RegraDeNegocioException(
                            "O produto informado não corresponde ao item " + itemDto.itemId() + ".");
                }
                if (!itensAtuaisUtilizados.add(itemExistente)) {
                    throw new RegraDeNegocioException("Um mesmo item não pode ser enviado mais de uma vez.");
                }
            } else {
                itemExistente = pedido.getItens().stream()
                        .filter(item -> item.getProdutoId().equals(itemDto.produtoId()))
                        .filter(itensAtuaisUtilizados::add)
                        .findFirst()
                        .orElse(null);
            }

            // Um produto removido do catálogo pode continuar associado a um pedido antigo.
            Produto produto = produtoRepositoryPort.buscarPorId(itemDto.produtoId())
                    .orElse(itemExistente != null ? itemExistente.getProduto() : null);
            if (produto == null) {
                throw new NaoEncontradoException("Produto não encontrado com o ID: " + itemDto.produtoId());
            }

            if (!produto.isAtivo() && !produtoIdsAtuais.contains(produto.getId())) {
                throw new RegraDeNegocioException(
                        "Produto inativo não pode ser adicionado ao pedido. ID: " + itemDto.produtoId());
            }

            ItemPedido novoItem = ItemPedido.comAcrescimo(
                    null,
                    produto.getId(),
                    itemDto.quantidade(),
                    itemDto.precoUnitario(),
                    itemDto.acrescimo() != null
                            ? itemDto.acrescimo()
                            : itemExistente != null ? itemExistente.getAcrescimo() : java.math.BigDecimal.ZERO,
                    itemDto.observacaoItem()
            );
            novoItem.setProduto(produto);
            novosItens.add(novoItem);
        }

        pedido.substituirItens(novosItens, descontoPedido);

        if (pedido.calcularTotalComprometido().compareTo(pedido.getValorTotal()) > 0) {
            throw new RegraDeNegocioException(
                    "O novo total do pedido não pode ser menor que os pagamentos confirmados ou pendentes.");
        }

        return pedidoRepositoryPort.atualizar(pedido);
    }
}
