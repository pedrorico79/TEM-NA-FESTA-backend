package com.temnafesta.application.usecase;

import com.temnafesta.application.dto.CriarUsuarioCommand;
import com.temnafesta.domain.exception.RegraDeNegocioException;
import com.temnafesta.domain.model.Perfil;
import com.temnafesta.domain.model.Usuario;
import com.temnafesta.domain.ports.repository.PerfilRepositoryPort;
import com.temnafesta.domain.ports.repository.UsuarioRepositoryPort;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

public class CriarUsuarioUseCase {

    private final UsuarioRepositoryPort usuarioRepositoryPort;
    private final PerfilRepositoryPort perfilRepositoryPort;
    private final PasswordEncoder passwordEncoder;

    public CriarUsuarioUseCase(UsuarioRepositoryPort usuarioRepositoryPort,
                              PerfilRepositoryPort perfilRepositoryPort,
                              PasswordEncoder passwordEncoder) {
        this.usuarioRepositoryPort = usuarioRepositoryPort;
        this.perfilRepositoryPort = perfilRepositoryPort;
        this.passwordEncoder = passwordEncoder;
    }

    public Usuario executar(CriarUsuarioCommand command) {
        if (command == null) {
            throw new RegraDeNegocioException("Dados do usuário são obrigatórios.");
        }
        if (command.nome() == null || command.nome().isBlank()) {
            throw new RegraDeNegocioException("O nome do usuário é obrigatório.");
        }
        if (command.email() == null || command.email().isBlank()) {
            throw new RegraDeNegocioException("O email do usuário é obrigatório.");
        }
        if (command.senha() == null || command.senha().isBlank()) {
            throw new RegraDeNegocioException("A senha do usuário é obrigatória.");
        }

        usuarioRepositoryPort.buscarPorEmail(command.email())
                .ifPresent(usuario -> {
                    throw new RegraDeNegocioException("Já existe um usuário cadastrado com este email.");
                });

        Perfil perfil = perfilRepositoryPort.buscarPorId(command.perfilId())
                .orElseThrow(() -> new IllegalArgumentException("Perfil não encontrado com o ID: " + command.perfilId()));

        Usuario usuario = new Usuario(
                null,
                command.nome().trim(),
                command.email().trim(),
                passwordEncoder.encode(command.senha()),
                true,
                false,
                LocalDateTime.now(),
                perfil
        );
        return usuarioRepositoryPort.salvar(usuario);
    }
}
