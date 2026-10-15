package com.temnafesta.presentation.dto;

import java.math.BigDecimal;

public record EvolucaoEventoPeriodoResponseDto(
        String periodo,
        Long pedidosTotais,
        Long vendasObtidas,
        BigDecimal faturamento,
        BigDecimal ticketMedio
) {}
