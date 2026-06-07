package com.temnafesta.dto.relatorio.produtosmaisvendidos;

import java.math.BigDecimal;

public interface MaisVendidosProjection {
    String getItem();
    Integer getQtdeVendida();
    BigDecimal getFaturamento();
    BigDecimal getPorcentagemDoTotal();
}


