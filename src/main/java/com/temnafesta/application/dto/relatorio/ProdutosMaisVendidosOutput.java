package com.temnafesta.application.dto.relatorio;


import java.math.BigDecimal;

public record ProdutosMaisVendidosOutput(
        String item,
        Integer qtdeVendida,
        BigDecimal faturamento,
        BigDecimal porcentagemDoTotal
) {}
