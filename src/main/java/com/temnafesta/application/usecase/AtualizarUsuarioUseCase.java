package com.temnafesta.application.usecase;

import com.temnafesta.domain.ports.repository.UsuarioRepositoryPort;

public class AtualizarUsuarioUseCase {

    private final UsuarioRepositoryPort usuarioRepositoryPort;

    public AtualizarUsuarioUseCase(UsuarioRepositoryPort usuarioRepositoryPort) {
        this.usuarioRepositoryPort = usuarioRepositoryPort;
    }
}
