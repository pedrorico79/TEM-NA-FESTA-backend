package com.temnafesta.presentation.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PagamentoResponseDto(
        Long id,
        BigDecimal valor,
        LocalDateTime dataPagamento,
        String tipoPagamento,
        String statusPagamento,
        Long metodoPagamentoId
) {}