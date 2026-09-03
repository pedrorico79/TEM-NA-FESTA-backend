package com.temnafesta.presentation.dto;

import java.math.BigDecimal;

public record ProdutoMaisVendidosResponseDto(
        String item,
        Integer qtdeVendida,
        BigDecimal faturamento,
        BigDecimal porcentagemDoTotal
) {}
