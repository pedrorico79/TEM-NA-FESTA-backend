package com.temnafesta.presentation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AtualizarUsuarioRequestDto(
        @NotBlank(message = "O nome do usuário é obrigatório.")
        String nome,

        @NotBlank(message = "O email do usuário é obrigatório.")
        String email,

        @NotNull(message = "O ID do perfil é obrigatório.")
        Long perfilId
) {
}
