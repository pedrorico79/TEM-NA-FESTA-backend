package com.temnafesta.dto.relatorio.pedidosPorPeriodo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface PedidosPeriodoProjection {
    Integer getId();
    LocalDateTime getDataPedido();
    String getClienteNome();
    String getEventoNome();
    BigDecimal getValorTotal();
    BigDecimal getValorPago();
    String getStatusNome();
}
