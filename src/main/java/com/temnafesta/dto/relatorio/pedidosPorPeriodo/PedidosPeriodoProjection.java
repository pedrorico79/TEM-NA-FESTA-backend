package com.temnafesta.dto.relatorio.pedidosPorPeriodo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface PedidosPeriodoProjection {
    Integer getId();
    LocalDateTime getDataPedido(); // ver se é dateTime mesmo
    String getClienteNome();
    String getEventoNome();
    BigDecimal getValorTotal();
    String getStatusNome();
}
