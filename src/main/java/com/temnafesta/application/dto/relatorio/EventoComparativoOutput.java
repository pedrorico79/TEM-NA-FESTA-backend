package com.temnafesta.application.dto.relatorio;

import java.math.BigDecimal;

public record EventoComparativoOutput(
        String evento,
        Long pedidosTotais,
        Long vendasObtidas,
        BigDecimal faturamento,
        BigDecimal ticketMedio
) {}
