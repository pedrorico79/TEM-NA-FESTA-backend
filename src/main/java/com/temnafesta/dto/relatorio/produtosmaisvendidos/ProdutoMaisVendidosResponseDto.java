package com.temnafesta.dto.relatorio.produtosmaisvendidos;

import java.math.BigDecimal;

public record ProdutoMaisVendidosResponseDto(
        String item,
        Integer qtdeVendida,
        BigDecimal faturamento,
        BigDecimal porcentagemDoTotal
) {}
