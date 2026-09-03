package com.temnafesta.application.dto.relatorio;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PedidosPeriodoOutput(
        Integer id,
        LocalDateTime dataPedido,
        String clienteNome,
        String eventoNome,
        BigDecimal valorTotal,
        BigDecimal valorPago,
        String statusNome
)
{}

