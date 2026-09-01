package com.temnafesta.application.usecase;

import com.temnafesta.domain.exception.RegraDeNegocioException;
import com.temnafesta.domain.model.Perfil;
import com.temnafesta.domain.model.Usuario;
import com.temnafesta.domain.ports.repository.PerfilRepositoryPort;
import com.temnafesta.domain.ports.repository.UsuarioRepositoryPort;

public class AtualizarUsuarioUseCase {

    private final UsuarioRepositoryPort usuarioRepositoryPort;
    private final PerfilRepositoryPort perfilRepositoryPort;

    public AtualizarUsuarioUseCase(UsuarioRepositoryPort usuarioRepositoryPort, PerfilRepositoryPort perfilRepositoryPort) {
        this.usuarioRepositoryPort = usuarioRepositoryPort;
        this.perfilRepositoryPort = perfilRepositoryPort;
    }

    public Usuario executar(Long id, String nome, String email, Long perfilId) {
        Usuario usuario = usuarioRepositoryPort.buscarPorId(id)
                .orElseThrow(() -> new RegraDeNegocioException("Usuário não encontrado com o ID: " + id));

        if (nome == null || nome.isBlank()) {
            throw new RegraDeNegocioException("O nome do usuário é obrigatório.");
        }
        if (email == null || email.isBlank()) {
            throw new RegraDeNegocioException("O email do usuário é obrigatório.");
        }

        Perfil perfil = perfilRepositoryPort.buscarPorId(perfilId)
                .orElseThrow(() -> new IllegalArgumentException("Perfil não encontrado com o ID: " + perfilId));

        Usuario atualizado = new Usuario(
                usuario.getId(),
                nome.trim(),
                email.trim(),
                usuario.getSenha(),
                usuario.isAtivo(),
                usuario.isDeletado(),
                usuario.getDataCriacao(),
                perfil
        );

        return usuarioRepositoryPort.atualizar(atualizado);
    }
}
