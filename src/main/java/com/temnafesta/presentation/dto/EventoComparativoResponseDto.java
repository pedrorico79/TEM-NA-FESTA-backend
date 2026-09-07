package com.temnafesta.presentation.dto;

import java.math.BigDecimal;

public record EventoComparativoResponseDto(
        String evento,
        Long pedidosTotais,
        Long vendasObtidas,
        BigDecimal faturamento,
        BigDecimal ticketMedio
) {}
