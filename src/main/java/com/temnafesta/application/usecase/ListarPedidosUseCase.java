package com.temnafesta.application.usecase;

import com.temnafesta.domain.model.Pedido;
import com.temnafesta.domain.ports.out.repository.PedidoRepositoryPort;
import com.temnafesta.domain.vo.StatusProducaoEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class ListarPedidosUseCase {

    private final PedidoRepositoryPort pedidoRepository;
    private final Clock relogioNegocio;

    public ListarPedidosUseCase(PedidoRepositoryPort pedidoRepository) {
        this(pedidoRepository, com.temnafesta.domain.time.RelogioNegocio.sistema());
    }

    public ListarPedidosUseCase(PedidoRepositoryPort pedidoRepository, Clock relogioNegocio) {
        this.pedidoRepository = pedidoRepository;
        this.relogioNegocio = relogioNegocio != null
                ? relogioNegocio
                : com.temnafesta.domain.time.RelogioNegocio.sistema();
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

    @Transactional(readOnly = true)
    public Page<Pedido> executarComFiltros(
            String busca,
            List<StatusProducaoEnum> status,
            Long eventoId,
            boolean pagamentoPendente,
            boolean ativos,
            boolean atrasados,
            Pageable pageable) {

        String buscaNormalizada = normalizarBusca(busca);
        List<StatusProducaoEnum> statusNormalizados = status == null || status.isEmpty()
                ? null
                : status.stream().distinct().toList();
        LocalDate dataReferenciaAtraso = LocalDate.now(relogioNegocio);

        return pedidoRepository.listarPedidos(
                buscaNormalizada,
                statusNormalizados,
                eventoId,
                pagamentoPendente,
                ativos,
                atrasados,
                dataReferenciaAtraso,
                pageable
        );
    }

    private String normalizarBusca(String busca) {
        if (busca == null) return null;
        String normalizada = busca.trim();
        return normalizada.isEmpty() ? null : normalizada;
    }
}
