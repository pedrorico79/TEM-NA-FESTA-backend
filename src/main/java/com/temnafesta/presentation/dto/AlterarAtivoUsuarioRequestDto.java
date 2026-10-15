package com.temnafesta.presentation.dto;

import jakarta.validation.constraints.NotNull;

public record AlterarAtivoUsuarioRequestDto(
        @NotNull(message = "O status ativo é obrigatório.")
        Boolean ativo
) {
}
