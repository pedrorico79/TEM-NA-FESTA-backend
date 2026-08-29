package com.temnafesta.presentation.dto;

import java.math.BigDecimal;

public record ItemPedidoResponseDto(
        Long id,
        Long produtoId,
        Integer quantidade,
        BigDecimal precoUnitario,
        String observacaoItem
) {}