package com.temnafesta.presentation.dto;

import jakarta.validation.constraints.NotBlank;

public record AlterarSenhaUsuarioRequestDto(
        @NotBlank(message = "A nova senha é obrigatória.")
        String senha
) {
}
