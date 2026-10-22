package com.temnafesta.presentation.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;

import java.math.BigDecimal;

public record ItemPedidoRequestDto(
        Long itemId,

        @NotNull(message = "O ID do produto é obrigatório.")
        Long produtoId,

        @NotNull(message = "O preço unitário é obrigatório.")
        @Min(value = 0, message = "O preço unitário não pode ser negativo.")
        BigDecimal precoUnitario,

        @DecimalMin(value = "0.00", message = "O acréscimo do item não pode ser negativo.")
        BigDecimal acrescimo,

        @NotNull(message = "A quantidade é obrigatória.")
        @Min(value = 1, message = "A quantidade mínima de um item é 1.")
        Integer quantidade,

        String observacaoItem
) {
    public ItemPedidoRequestDto(Long produtoId, BigDecimal precoUnitario,
                                Integer quantidade, String observacaoItem) {
        this(null, produtoId, precoUnitario, BigDecimal.ZERO, quantidade, observacaoItem);
    }
}
