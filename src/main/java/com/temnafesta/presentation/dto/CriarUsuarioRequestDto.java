package com.temnafesta.presentation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CriarUsuarioRequestDto(

        @NotNull(message = "O ID do usuário é obrigatório.")
        Long id,

        @NotBlank(message = "O nome do usuário é obrigatório.")
        String nome,

        @NotBlank(message = "O email do usuário é obrigatório.")
        String email,

        @NotBlank(message = "A senha do usuário é obrigatória.")
        String senha,

        @NotNull(message = "O ID do perfil é obrigatório.")
        Long perfilId
) {

}
