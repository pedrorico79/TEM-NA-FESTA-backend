package com.temnafesta.application.usecase;

import com.temnafesta.application.dto.AtualizarPedidoCommand;
import com.temnafesta.domain.exception.RegraDeNegocioException;
import com.temnafesta.domain.model.ItemPedido;
import com.temnafesta.domain.model.Pedido;
import com.temnafesta.domain.model.Produto;
import com.temnafesta.domain.ports.repository.PedidoRepositoryPort;
import com.temnafesta.domain.ports.repository.ProdutoRepositoryPort;

import java.util.ArrayList;
import java.util.List;

public class AtualizarPedidoUseCase {

    private final PedidoRepositoryPort pedidoRepositoryPort;
    private final ProdutoRepositoryPort produtoRepositoryPort;

    public AtualizarPedidoUseCase(PedidoRepositoryPort pedidoRepositoryPort, ProdutoRepositoryPort produtoRepositoryPort) {
        this.pedidoRepositoryPort = pedidoRepositoryPort;
        this.produtoRepositoryPort = produtoRepositoryPort;
    }

    public Pedido executar(AtualizarPedidoCommand command) {
        // 1. Busca o pedido existente
        Pedido pedido = pedidoRepositoryPort.buscarPorId(command.pedidoId())
                .orElseThrow(() -> new RegraDeNegocioException("Pedido não encontrado com o ID: " + command.pedidoId()));

        // 2. Atualiza dados de capa e logística
        pedido.atualizarDadosBasicos(
                command.dataEntrega(),
                command.taxaEntrega(),
                command.observacao(),
                command.enderecoEntregaId()
        );

        // 3. Monta a nova lista de itens acatando o preço negociado na interface
        List<ItemPedido> novosItens = new ArrayList<>();
        for (AtualizarPedidoCommand.ItemCommand itemDto : command.itens()) {

            // Garante que o produto existe
            Produto produto = produtoRepositoryPort.buscarPorId(itemDto.produtoId())
                    .orElseThrow(() -> new RegraDeNegocioException("Produto não encontrado com o ID: " + itemDto.produtoId()));

            novosItens.add(new ItemPedido(
                    null,
                    produto.getId(),
                    itemDto.quantidade(),
                    itemDto.precoUnitario(), // <-- O sistema recebe o preço definido na requisição com desconto/acréscimo
                    itemDto.observacaoItem()
            ));
        }

        // 4. Delega a substituição segura para a Raiz de Agregação (recalcula total automaticamente)
        pedido.substituirItens(novosItens);

        // 5. Persiste as alterações no banco de dados
        return pedidoRepositoryPort.atualizar(pedido);
    }
}