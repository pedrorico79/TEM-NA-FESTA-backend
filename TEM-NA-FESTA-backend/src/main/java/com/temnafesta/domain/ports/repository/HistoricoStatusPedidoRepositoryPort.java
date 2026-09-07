package com.temnafesta.domain.ports.repository;

import com.temnafesta.domain.model.HistoricoStatusPedido;
import java.util.List;

public interface HistoricoStatusPedidoRepositoryPort {
    HistoricoStatusPedido salvar(HistoricoStatusPedido historico);
    List<HistoricoStatusPedido> buscarPorPedidoId(Long pedidoId);
}