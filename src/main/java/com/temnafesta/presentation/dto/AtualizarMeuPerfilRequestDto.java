package com.temnafesta.presentation.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record AtualizarMeuPerfilRequestDto(
        @NotBlank(message = "O nome do usuário é obrigatório.")
        String nome,

        @NotBlank(message = "O email do usuário é obrigatório.")
        @Email(message = "O email do usuário deve ser um endereço de email válido.")
        String email
) {
}
