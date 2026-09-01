package com.temnafesta.presentation.dto;

import jakarta.validation.constraints.NotNull;

public record AlterarStatusEventoRequestDto(
        @NotNull(message = "O status do evento é obrigatório.")
        Boolean ativo
) {
}
