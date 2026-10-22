package com.temnafesta.infrastructure.projection;

import java.math.BigDecimal;

public interface EvolucaoEventoProjection {
    String getPeriodo();
    Long getPedidosTotais();
    Long getVendasObtidas();
    BigDecimal getFaturamento();
    BigDecimal getTicketMedio();
}
