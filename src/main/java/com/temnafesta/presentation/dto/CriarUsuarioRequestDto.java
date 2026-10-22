package com.temnafesta.presentation.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CriarUsuarioRequestDto(

        @NotBlank(message = "O nome do usuário é obrigatório.")
        String nome,

        @NotBlank(message = "O email do usuário é obrigatório.")
        @Email(message = "O email do usuário deve ser um endereço de email válido.")
        String email,

        @NotBlank(message = "A senha do usuário é obrigatória.")
        @Size(min = 8, max = 72, message = "A senha deve ter entre 8 e 72 caracteres.")
        String senha,

        @NotNull(message = "O ID do perfil é obrigatório.")
        Long perfilId
) {

}
