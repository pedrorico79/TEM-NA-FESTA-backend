package com.temnafesta.application.usecase;

import com.temnafesta.domain.exception.RegraDeNegocioException;
import com.temnafesta.domain.model.HistoricoStatusPedido;
import com.temnafesta.domain.ports.repository.HistoricoStatusPedidoRepositoryPort;
import com.temnafesta.domain.ports.repository.PedidoRepositoryPort;

import java.util.List;

public class ListarHistoricoStatusPedidoUseCase {

    private final PedidoRepositoryPort pedidoRepositoryPort;
    private final HistoricoStatusPedidoRepositoryPort historicoRepositoryPort;

    public ListarHistoricoStatusPedidoUseCase(PedidoRepositoryPort pedidoRepositoryPort,
                                              HistoricoStatusPedidoRepositoryPort historicoRepositoryPort) {
        this.pedidoRepositoryPort = pedidoRepositoryPort;
        this.historicoRepositoryPort = historicoRepositoryPort;
    }

    public List<HistoricoStatusPedido> executar(Long pedidoId) {
        pedidoRepositoryPort.buscarPorId(pedidoId)
                .orElseThrow(() -> new RegraDeNegocioException("Pedido não encontrado com ID: " + pedidoId));

        return historicoRepositoryPort.buscarPorPedidoId(pedidoId);
    }
}
