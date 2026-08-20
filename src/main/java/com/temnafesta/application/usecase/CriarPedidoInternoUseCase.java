package com.temnafesta.application.usecase;

import com.temnafesta.application.dto.CriarPedidoCommand;
import com.temnafesta.domain.exception.RegraDeNegocioException;
import com.temnafesta.domain.model.ItemPedido;
import com.temnafesta.domain.model.Pedido;
import com.temnafesta.domain.model.Produto;
import com.temnafesta.domain.ports.repository.ClienteRepositoryPort;
import com.temnafesta.domain.ports.repository.PedidoRepositoryPort;
import com.temnafesta.domain.ports.repository.ProdutoRepositoryPort;

public class CriarPedidoInternoUseCase {

    private final PedidoRepositoryPort pedidoRepositoryPort;
    private final ClienteRepositoryPort clienteRepositoryPort;
    private final ProdutoRepositoryPort produtoRepositoryPort;

    public CriarPedidoInternoUseCase(PedidoRepositoryPort pedidoRepositoryPort,
                                     ClienteRepositoryPort clienteRepositoryPort,
                                     ProdutoRepositoryPort produtoRepositoryPort) {
        this.pedidoRepositoryPort = pedidoRepositoryPort;
        this.clienteRepositoryPort = clienteRepositoryPort;
        this.produtoRepositoryPort = produtoRepositoryPort;
    }

    public Pedido executar(CriarPedidoCommand command) {
        // 1. Valida existência do Cliente
        clienteRepositoryPort.buscarPorId(command.clienteId())
                .orElseThrow(() -> new RegraDeNegocioException("Cliente não encontrado com o ID: " + command.clienteId()));

        // 2. Instancia a Entidade Pedido
        Pedido novoPedido = new Pedido(
                null,
                null, // dataPedido assume o momento atual
                command.dataEntrega(),
                command.taxaEntrega(),
                command.observacao(),
                command.clienteId(),
                command.usuarioId(),
                command.eventoId(),
                command.enderecoEntregaId()
        );

        // 3. Monta e adiciona os itens com o preço unitário congelado do cadastro
        for (CriarPedidoCommand.ItemCommand itemDto : command.itens()) {
            Produto produto = produtoRepositoryPort.buscarPorId(itemDto.produtoId())
                    .orElseThrow(() -> new RegraDeNegocioException("Produto não encontrado com o ID: " + itemDto.produtoId()));

            ItemPedido item = new ItemPedido(
                    null,
                    produto.getId(),
                    itemDto.quantidade(),
                    produto.getPrecoVenda(), // Preço atual do cardápio congelado no item
                    itemDto.observacaoItem()
            );

            novoPedido.adicionarItem(item);
        }

        // 4. Persistir o pedido no banco através da Port
        return pedidoRepositoryPort.salvar(novoPedido);
    }
}