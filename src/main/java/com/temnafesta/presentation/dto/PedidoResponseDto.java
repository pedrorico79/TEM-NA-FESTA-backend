package com.temnafesta.presentation.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PedidoResponseDto(
        Long id,
        LocalDateTime dataPedido,
        LocalDateTime dataEntrega,
        BigDecimal valorTotal,
        BigDecimal taxaEntrega,
        String statusProducao,
        Long clienteId
) {}