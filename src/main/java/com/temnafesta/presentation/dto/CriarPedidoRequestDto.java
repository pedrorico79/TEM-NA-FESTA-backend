package com.temnafesta.presentation.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record CriarPedidoRequestDto(
        @NotNull(message = "O ID do cliente é obrigatório.")
        Long clienteId,

        @NotNull(message = "A data de entrega é obrigatória.")
        @FutureOrPresent(message = "A data de entrega não pode ser no passado.")
        LocalDateTime dataEntrega,

        @Min(value = 0, message = "A taxa de entrega não pode ser um valor negativo.")
        BigDecimal taxaEntrega,

        String observacao,

        Long eventoId,

        Long enderecoEntregaId,

        @NotEmpty(message = "O pedido deve conter pelo menos um item.")
        @Valid
        List<ItemPedidoRequestDto> itens
) {}