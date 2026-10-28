package com.temnafesta.application.usecase;

import com.temnafesta.domain.exception.NaoEncontradoException;
import com.temnafesta.domain.exception.RegraDeNegocioException;
import com.temnafesta.domain.model.Perfil;
import com.temnafesta.domain.model.Usuario;
import com.temnafesta.domain.ports.out.repository.PerfilRepositoryPort;
import com.temnafesta.domain.ports.out.repository.UsuarioRepositoryPort;

import java.util.Locale;

public class AtualizarUsuarioUseCase {

    private final UsuarioRepositoryPort usuarioRepositoryPort;
    private final PerfilRepositoryPort perfilRepositoryPort;

    public AtualizarUsuarioUseCase(UsuarioRepositoryPort usuarioRepositoryPort, PerfilRepositoryPort perfilRepositoryPort) {
        this.usuarioRepositoryPort = usuarioRepositoryPort;
        this.perfilRepositoryPort = perfilRepositoryPort;
    }

    public Usuario executar(Long id, String nome, String email, Long perfilId) {
        Usuario usuario = usuarioRepositoryPort.buscarPorId(id)
                .orElseThrow(() -> new NaoEncontradoException("Usuário não encontrado com o ID: " + id));

        validarDados(id, nome, email);

        Perfil perfil = perfilRepositoryPort.buscarPorId(perfilId)
                .orElseThrow(() -> new NaoEncontradoException("Perfil não encontrado com o ID: " + perfilId));

        Usuario atualizado = new Usuario(
                usuario.getId(),
                nome.trim(),
                normalizarEmail(email),
                usuario.getSenha(),
                usuario.isAtivo(),
                usuario.isDeletado(),
                usuario.getDataCriacao(),
                perfil
        );

        return usuarioRepositoryPort.atualizar(atualizado);
    }

    public Usuario executar(Long id, String nome, String email) {
        Usuario usuario = usuarioRepositoryPort.buscarPorId(id)
                .orElseThrow(() -> new NaoEncontradoException("Usuário não encontrado com o ID: " + id));

        validarDados(id, nome, email);
        Usuario atualizado = new Usuario(
                usuario.getId(),
                nome.trim(),
                normalizarEmail(email),
                usuario.getSenha(),
                usuario.isAtivo(),
                usuario.isDeletado(),
                usuario.getDataCriacao(),
                usuario.getPerfil()
        );
        return usuarioRepositoryPort.atualizar(atualizado);
    }

    private void validarDados(Long id, String nome, String email) {
        if (nome == null || nome.isBlank()) {
            throw new RegraDeNegocioException("O nome do usuário é obrigatório.");
        }
        if (email == null || email.isBlank()) {
            throw new RegraDeNegocioException("O email do usuário é obrigatório.");
        }

        String emailNormalizado = normalizarEmail(email);
        usuarioRepositoryPort.buscarPorEmail(emailNormalizado)
                .filter(usuarioExistente -> !usuarioExistente.getId().equals(id))
                .ifPresent(usuarioExistente -> {
                    throw new RegraDeNegocioException("Já existe um usuário cadastrado com este email.");
                });
    }

    private String normalizarEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }
}
