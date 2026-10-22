package com.temnafesta.presentation.dto;

import java.math.BigDecimal;

public record ItemPedidoResponseDto(
        Long id,
        Integer quantidade,
        BigDecimal precoUnitario,
        BigDecimal acrescimo,
        String observacaoItem,
        ProdutoResponseDto produto
) {}
