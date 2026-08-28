package com.temnafesta.application.usecase;

import com.temnafesta.domain.ports.repository.UsuarioRepositoryPort;

public class ListarUsuarioUseCase {

    private final UsuarioRepositoryPort usuarioRepositoryPort;

    public ListarUsuarioUseCase(UsuarioRepositoryPort usuarioRepositoryPort) {
        this.usuarioRepositoryPort = usuarioRepositoryPort;
    }
}
