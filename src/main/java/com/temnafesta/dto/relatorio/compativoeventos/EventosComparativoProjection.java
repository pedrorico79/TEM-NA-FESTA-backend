package com.temnafesta.dto.relatorio.compativoeventos;

import java.math.BigDecimal;

public interface EventosComparativoProjection {
    String getEvento();
    Long getPedidosTotais();
    Long getVendasObtidas();
    BigDecimal getFaturamento();
    BigDecimal getTicketMedio();
}
