package com.temnafesta.dto.relatorio.kpi;

import java.math.BigDecimal;

public record KpiResponseDto(
        Long totalPedidos,
        Long totalEntregues,
        Double taxaConclusaoPorcentagem,
        BigDecimal faturamentoTotal,
        Long periodoDias
) {}
