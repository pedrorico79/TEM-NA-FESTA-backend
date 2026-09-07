package com.temnafesta.presentation.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PedidosPeriodoResponseDto(
        Integer id,
        LocalDateTime dataPedido,
        String clienteNome,
        String eventoNome,
        BigDecimal valorTotal,
        BigDecimal valorPago,
        String statusNome
) {}
