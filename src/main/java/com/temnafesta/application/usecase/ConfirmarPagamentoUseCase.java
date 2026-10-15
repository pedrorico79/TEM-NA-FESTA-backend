package com.temnafesta.application.usecase;

import com.temnafesta.domain.model.Pedido;
import com.temnafesta.domain.ports.repository.PedidoRepositoryPort;
import com.temnafesta.application.exception.RecursoNaoEncontradoException;

public class ConfirmarPagamentoUseCase {
    private final PedidoRepositoryPort pedidoRepositoryPort;

    public ConfirmarPagamentoUseCase(PedidoRepositoryPort pedidoRepositoryPort) {
        this.pedidoRepositoryPort = pedidoRepositoryPort;
    }

    public Pedido executar(Long pedidoId, Long pagamentoId) {
        Pedido pedido = pedidoRepositoryPort.buscarPorIdParaRegistrarPagamento(pedidoId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Pedido não encontrado."));
        pedido.confirmarPagamento(pagamentoId);
        return pedidoRepositoryPort.salvar(pedido);
    }
}
