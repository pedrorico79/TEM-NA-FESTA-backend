package com.temnafesta.application.usecase;

import com.temnafesta.application.exception.RecursoNaoEncontradoException;
import com.temnafesta.domain.model.Usuario;
import com.temnafesta.domain.ports.out.repository.UsuarioRepositoryPort;

public class AlterarAtivoUsuarioUseCase {
    private final UsuarioRepositoryPort usuarioRepositoryPort;

    public AlterarAtivoUsuarioUseCase(UsuarioRepositoryPort usuarioRepositoryPort) {
        this.usuarioRepositoryPort = usuarioRepositoryPort;
    }

    public Usuario executar(Long id, boolean ativo) {
        Usuario usuario = usuarioRepositoryPort.buscarPorId(id)
                .filter(usuarioEncontrado -> !usuarioEncontrado.isDeletado())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado com o ID: " + id));

        if (usuario.isAtivo() == ativo) return usuario;

        Usuario atualizado = new Usuario(
                usuario.getId(), usuario.getNome(), usuario.getEmail(), usuario.getSenha(),
                ativo, usuario.isDeletado(), usuario.getDataCriacao(), usuario.getPerfil());
        return usuarioRepositoryPort.atualizar(atualizado);
    }
}
