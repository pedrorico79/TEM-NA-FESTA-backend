package com.temnafesta.presentation.dto;

import com.temnafesta.infrastructure.security.user.UsuarioAutenticado;
import org.springframework.security.core.GrantedAuthority;

public record MeResponseDto(
        Long id,
        String nome,
        String email,
        String perfil
) {
    public static MeResponseDto from(UsuarioAutenticado user) {
        return new MeResponseDto(user.getId(), user.getNome(), user.getUsername(), getRole(user));
    }

    private static String getRole(UsuarioAutenticado user) {
        return user.getAuthorities().stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .map(auth -> auth.replace("ROLE_", ""))
                .orElse("CLIENTE");
    }
}