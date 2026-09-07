package com.temnafesta.application.usecase;

import com.temnafesta.domain.exception.RegraDeNegocioException;
import com.temnafesta.domain.model.Pedido;
import com.temnafesta.domain.ports.repository.PedidoRepositoryPort;

public class ExcluirPagamentoUseCase {

    private final PedidoRepositoryPort pedidoRepositoryPort;

    public ExcluirPagamentoUseCase(PedidoRepositoryPort pedidoRepositoryPort) {
        this.pedidoRepositoryPort = pedidoRepositoryPort;
    }

    public void executar(Long pedidoId, Long pagamentoId) {
        Pedido pedido = pedidoRepositoryPort.buscarPorId(pedidoId)
                .orElseThrow(() -> new RegraDeNegocioException("Pedido não encontrado com ID: " + pedidoId));

        // Remove o pagamento do agregado. A persistência (orphanRemoval) garante o hard delete no banco.
        pedido.removerPagamento(pagamentoId);

        pedidoRepositoryPort.salvar(pedido);
    }
}
