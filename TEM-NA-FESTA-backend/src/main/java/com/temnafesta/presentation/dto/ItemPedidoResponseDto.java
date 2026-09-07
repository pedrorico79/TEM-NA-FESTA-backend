package com.temnafesta.presentation.dto;

import java.math.BigDecimal;

public record ItemPedidoResponseDto(
        Long id,
        Integer quantidade,
        BigDecimal precoUnitario,
        String observacaoItem,
        ProdutoResponseDto produto
) {}