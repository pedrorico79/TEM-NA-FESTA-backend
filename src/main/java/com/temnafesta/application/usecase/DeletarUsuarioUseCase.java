package com.temnafesta.application.usecase;

import com.temnafesta.domain.ports.repository.UsuarioRepositoryPort;

public class DeletarUsuarioUseCase {

    private final UsuarioRepositoryPort usuarioRepositoryPort;

    public DeletarUsuarioUseCase(UsuarioRepositoryPort usuarioRepositoryPort) {
        this.usuarioRepositoryPort = usuarioRepositoryPort;
    }
}
