package com.temnafesta.application.usecase;

import com.temnafesta.domain.exception.RegraDeNegocioException;
import com.temnafesta.domain.model.Pedido;
import com.temnafesta.domain.ports.repository.PedidoRepositoryPort;

public class ExcluirPagamentoUseCase {

    private final PedidoRepositoryPort pedidoRepositoryPort;

    public ExcluirPagamentoUseCase(PedidoRepositoryPort pedidoRepositoryPort) {
        this.pedidoRepositoryPort = pedidoRepositoryPort;
    }

    public Pedido executar(Long pedidoId, Long pagamentoId) {
        Pedido pedido = pedidoRepositoryPort.buscarPorIdParaRegistrarPagamento(pedidoId)
                .orElseThrow(() -> new RegraDeNegocioException("Pedido não encontrado com ID: " + pedidoId));

        pedido.cancelarPagamento(pagamentoId);

        return pedidoRepositoryPort.salvar(pedido);
    }
}
