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

    public Page<Usuario> executar(String nome, boolean ativos, Pageable pageable) {
        if (!ativos) return usuarioRepositoryPort.listarPorNomePaginado(nome, pageable);
        return usuarioRepositoryPort.listarPorNomePaginado(nome, ativos, pageable);
    }

    public Page<Usuario> executar(String nome, Pageable pageable) {
        return executar(nome, false, pageable);
    }
}
