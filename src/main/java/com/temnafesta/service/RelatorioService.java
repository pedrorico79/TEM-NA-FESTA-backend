package com.temnafesta.service;

import com.temnafesta.dto.relatorio.kpi.KpiResponseDto;
import com.temnafesta.repository.PedidoRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

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
        Long dias = ChronoUnit.DAYS.between(de, ate);
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
}
