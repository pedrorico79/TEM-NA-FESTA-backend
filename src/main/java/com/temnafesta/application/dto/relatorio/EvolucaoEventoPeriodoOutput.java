package com.temnafesta.application.dto.relatorio;

import java.math.BigDecimal;

public record EvolucaoEventoPeriodoOutput(
        String periodo,
        Long pedidosTotais,
        Long vendasObtidas,
        BigDecimal faturamento,
        BigDecimal ticketMedio
) {}
