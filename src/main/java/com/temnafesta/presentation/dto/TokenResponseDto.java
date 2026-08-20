package com.temnafesta.presentation.dto;

import jakarta.validation.constraints.NotBlank;

public record TokenResponseDto(
        @NotBlank(message = "O token é obrigatório.")
        String token,
        @NotBlank(message = "O tipo é obrigatório.")
        String tipo // Ex: "Bearer"
) {}