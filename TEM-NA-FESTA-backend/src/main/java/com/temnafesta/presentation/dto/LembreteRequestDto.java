package com.temnafesta.presentation.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record LembreteRequestDto(
        @NotBlank(message = "A descrição do lembrete não pode estar vazia.")
        String descricao,

        @NotNull(message = "A data limite é obrigatória.")
        @FutureOrPresent(message = "A data limite não pode ser no passado.")
        LocalDate dataLimite
) {}