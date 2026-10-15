package com.temnafesta.application.usecase;

import com.temnafesta.application.exception.RecursoNaoEncontradoException;
import com.temnafesta.domain.model.Usuario;
import com.temnafesta.domain.ports.repository.UsuarioRepositoryPort;
import org.springframework.security.crypto.password.PasswordEncoder;

public class AlterarSenhaUsuarioUseCase {
    private final UsuarioRepositoryPort usuarioRepositoryPort;
    private final PasswordEncoder passwordEncoder;

    public AlterarSenhaUsuarioUseCase(UsuarioRepositoryPort usuarioRepositoryPort, PasswordEncoder passwordEncoder) {
        this.usuarioRepositoryPort = usuarioRepositoryPort;
        this.passwordEncoder = passwordEncoder;
    }

    public void executar(Long id, String senha) {
        Usuario usuario = usuarioRepositoryPort.buscarPorId(id)
                .filter(usuarioEncontrado -> !usuarioEncontrado.isDeletado())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado com o ID: " + id));

        Usuario atualizado = new Usuario(
                usuario.getId(), usuario.getNome(), usuario.getEmail(), passwordEncoder.encode(senha),
                usuario.isAtivo(), usuario.isDeletado(), usuario.getDataCriacao(), usuario.getPerfil());
        usuarioRepositoryPort.atualizar(atualizado);
    }
}
