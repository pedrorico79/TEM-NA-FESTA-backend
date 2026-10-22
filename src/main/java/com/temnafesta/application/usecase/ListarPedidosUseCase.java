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
        return executar(busca, status, eventoId, false, false, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Pedido> executar(
            String busca,
            StatusProducaoEnum status,
            Long eventoId,
            boolean pagamentoPendente,
            Pageable pageable) {
        return executar(busca, status, eventoId, pagamentoPendente, false, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Pedido> executar(
            String busca,
            StatusProducaoEnum status,
            Long eventoId,
            boolean pagamentoPendente,
            boolean ativos,
            Pageable pageable) {

        busca = normalizarBusca(busca);

        if (!pagamentoPendente && !ativos) {
            return pedidoRepository.listarPedidos(busca, status, eventoId, pageable);
        }

        return pedidoRepository.listarPedidos(
                busca,
                null,
                eventoId,
                pagamentoPendente,
                ativos,
                pageable
        );
    }

    private String normalizarBusca(String busca) {
        if (busca == null) return null;
        String normalizada = busca.trim();
        return normalizada.isEmpty() ? null : normalizada;
    }
}
