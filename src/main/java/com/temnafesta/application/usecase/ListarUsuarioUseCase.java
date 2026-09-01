package com.temnafesta.application.usecase;

import com.temnafesta.domain.model.Usuario;
import com.temnafesta.domain.ports.repository.UsuarioRepositoryPort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class ListarUsuarioUseCase {

    private final UsuarioRepositoryPort usuarioRepositoryPort;

    public ListarUsuarioUseCase(UsuarioRepositoryPort usuarioRepositoryPort) {
        this.usuarioRepositoryPort = usuarioRepositoryPort;
    }

    public Page<Usuario> executar(String nome, Pageable pageable) {
        return usuarioRepositoryPort.listarPorNomePaginado(nome, pageable);
    }
}
