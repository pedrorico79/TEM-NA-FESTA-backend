package com.temnafesta.service;

import com.temnafesta.dto.relatorio.kpi.KpiResponseDto;
import com.temnafesta.dto.relatorio.pedidosPorPeriodo.PedidosPeriodoProjection;
import com.temnafesta.dto.relatorio.pedidosPorPeriodo.PedidosPeriodoResponseDto;
import com.temnafesta.dto.relatorio.pedidosporsemana.PedidosPorSemanaProjection;
import com.temnafesta.dto.relatorio.pedidosporsemana.PedidosPorSemanaResponseDto;
import com.temnafesta.repository.PedidoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class RelatorioService {

    private final PedidoRepository pedidoRepository;

    public RelatorioService(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    public KpiResponseDto obterKpis(
            LocalDate de,
            LocalDate ate
    ) {
        Long dias = ChronoUnit.DAYS.between(de, ate); // intervalo entre as datas
        Integer statusEntregueProducao = 4; // alterar com base no Id correspondente no bd.

        Long totalPedidos = pedidoRepository.countByDataPedidoBetween(
                de.atStartOfDay(),
                ate.atTime(23, 59, 59)
        );


        Long totalEntregues = pedidoRepository.countByStatusEPeriodo(
                statusEntregueProducao,
                de.atStartOfDay(),
                ate.atTime(23, 59, 59)
        );

        BigDecimal faturamento = pedidoRepository.somarFaturamentoNoPeriodo(
                statusEntregueProducao,
                de.atStartOfDay(),
                ate.atTime(23, 59, 59)
        );

        double taxaConclusao =  0.0;
        if (totalPedidos > 0) {
            taxaConclusao = ((double)totalEntregues / totalPedidos) * 100; // sem esse (double) o java conta errado
        }

        return new KpiResponseDto(
                totalPedidos,
                totalEntregues,
                taxaConclusao,
                faturamento,
                dias
        );
    }

    public List<PedidosPorSemanaResponseDto> retornarPedidosPorSemana(
            LocalDate de,
            LocalDate ate
    ) {
        List<PedidosPorSemanaProjection> resultado = pedidoRepository.buscarPedidosAgrupadosPorSemana(
                de.atStartOfDay(),
                ate.atTime(23, 59, 59)
        );

        return resultado.stream()
                .map(p -> new PedidosPorSemanaResponseDto(p.getRotulo(), p.getQuantidade()))
                .toList();
    }

    public Page<PedidosPeriodoResponseDto> retornarPedidosPeriodo(LocalDate de, LocalDate ate, Pageable pageable) {
        Page<PedidosPeriodoProjection> pedidosPaginados = pedidoRepository.buscarPedidosPeriodoPaginado(
                de.atStartOfDay(),
                ate.atTime(23, 59, 59),
                pageable
        );

        return pedidosPaginados.map(p -> new PedidosPeriodoResponseDto(
                p.getId(),
                p.getDataPedido(),
                p.getClienteNome(),
                p.getEventoNome(),
                p.getValorTotal(),
                p.getStatusNome()
        ));
    }

}
