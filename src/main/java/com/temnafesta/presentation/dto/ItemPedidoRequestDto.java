package com.temnafesta.presentation.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ItemPedidoRequestDto(
        @NotNull(message = "O ID do produto é obrigatório.")
        Long produtoId,

        @NotNull(message = "O preço unitário é obrigatório.")
        @Min(value = 0, message = "O preço unitário não pode ser negativo.")
        BigDecimal precoUnitario,

        @NotNull(message = "A quantidade é obrigatória.")
        @Min(value = 1, message = "A quantidade mínima de um item é 1.")
        Integer quantidade,

        String observacaoItem
) {}