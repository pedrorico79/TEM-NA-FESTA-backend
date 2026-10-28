package com.temnafesta.application.usecase;

import com.temnafesta.domain.model.Pedido;
import com.temnafesta.domain.ports.out.repository.PedidoRepositoryPort;
import com.temnafesta.domain.exception.NaoEncontradoException;
import org.springframework.transaction.annotation.Transactional;

public class ConfirmarPagamentoUseCase {
    private final PedidoRepositoryPort pedidoRepositoryPort;

    public ConfirmarPagamentoUseCase(PedidoRepositoryPort pedidoRepositoryPort) {
        this.pedidoRepositoryPort = pedidoRepositoryPort;
    }

    @Transactional
    public Pedido executar(Long pedidoId, Long pagamentoId) {
        Pedido pedido = pedidoRepositoryPort.buscarPorIdParaRegistrarPagamento(pedidoId)
                .orElseThrow(() -> new NaoEncontradoException("Pedido não encontrado."));
        pedido.confirmarPagamento(pagamentoId);
        return pedidoRepositoryPort.salvar(pedido);
    }
}
