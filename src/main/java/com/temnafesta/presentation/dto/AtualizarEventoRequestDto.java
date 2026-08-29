package com.temnafesta.presentation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record AtualizarEventoRequestDto(
        @NotBlank(message = "O nome do evento é obrigatório.")
        String nome,

        @NotNull(message = "A data de início do evento é obrigatória.")
        LocalDate dataInicio,

        @NotNull(message = "A data de fim do evento é obrigatória.")
        LocalDate dataFim
) {
}
