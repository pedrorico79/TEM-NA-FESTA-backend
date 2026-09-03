package com.temnafesta.application.dto.relatorio;

import java.math.BigDecimal;

public record KpiOutput(
        Long totalPedidos,
        Long totalEntregues,
        Double taxaConclusaoPorcentagem,
        BigDecimal faturamentoTotal,
        Long periodoDias)
{}
