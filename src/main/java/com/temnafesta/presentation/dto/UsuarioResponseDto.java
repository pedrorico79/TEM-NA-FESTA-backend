package com.temnafesta.presentation.dto;

import com.temnafesta.domain.model.Perfil;

import java.time.LocalDateTime;

public record UsuarioResponseDto(
        Long id,
        String nome,
        String email,
        boolean ativo,
        boolean deletado,
        LocalDateTime dataCriacao,
        Perfil perfil
) {
}
