package com.temnafesta.application.usecase;

import com.temnafesta.domain.model.Pedido;
import com.temnafesta.domain.ports.repository.PedidoRepositoryPort;
import com.temnafesta.domain.vo.StatusProducaoEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

public class ListarPedidosUseCase {

    private final PedidoRepositoryPort pedidoRepository;

    public ListarPedidosUseCase(PedidoRepositoryPort pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    @Transactional(readOnly = true)
    public Page<Pedido> executar(
            String busca,
            StatusProducaoEnum status,
            Long eventoId,
            Pageable pageable) {

        if (busca != null) {
            busca = busca.trim();

            if (busca.isEmpty()) {
                busca = null;
            }
        }

        return pedidoRepository.listarPedidos(
                busca,
                status,
                eventoId,
                pageable
        );
    }
}