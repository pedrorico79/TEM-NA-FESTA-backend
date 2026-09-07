package com.temnafesta.application.usecase;

import com.temnafesta.domain.model.Pedido;
import com.temnafesta.domain.ports.repository.PedidoRepositoryPort;
import com.temnafesta.domain.vo.StatusProducaoEnum;

import java.util.List;

public class ListarPedidosUseCase {

    private final PedidoRepositoryPort pedidoRepository;

    public ListarPedidosUseCase(PedidoRepositoryPort pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    public List<Pedido> executar(
            String busca,
            StatusProducaoEnum status,
            Long eventoId) {

        if (busca != null) {
            busca = busca.trim();

            if (busca.isEmpty()) {
                busca = null;
            }
        }

        return pedidoRepository.listarPedidos(
                busca,
                status,
                eventoId
        );
    }
}