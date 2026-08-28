package com.temnafesta.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CriarUsuarioCommand(
        Long id,
        String nome,
        String email,
        String senha,
        Long perfilId) {
}
