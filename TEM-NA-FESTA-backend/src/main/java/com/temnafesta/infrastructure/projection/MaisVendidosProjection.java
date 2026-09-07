package com.temnafesta.infrastructure.projection;

import java.math.BigDecimal;

public interface MaisVendidosProjection {
    String getItem();
    Integer getQtdeVendida();
    BigDecimal getFaturamento();
    BigDecimal getPorcentagemDoTotal();
}
