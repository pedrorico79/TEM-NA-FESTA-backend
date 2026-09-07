package com.temnafesta.infrastructure.projection;

import java.math.BigDecimal;

public interface EventosComparativoProjection {
    String getEvento();
    Long getPedidosTotais();
    Long getVendasObtidas();
    BigDecimal getFaturamento();
    BigDecimal getTicketMedio();
}
