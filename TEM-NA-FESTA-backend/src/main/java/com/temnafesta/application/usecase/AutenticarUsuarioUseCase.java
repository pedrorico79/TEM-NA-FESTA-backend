package com.temnafesta.application.usecase;

import com.temnafesta.domain.model.Usuario;
import com.temnafesta.domain.ports.repository.UsuarioRepositoryPort;
import com.temnafesta.infrastructure.security.jwt.JwtTokenProvider;
import org.springframework.security.crypto.password.PasswordEncoder;

public class AutenticarUsuarioUseCase {

    private final UsuarioRepositoryPort usuarioRepositoryPort;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public AutenticarUsuarioUseCase(UsuarioRepositoryPort usuarioRepositoryPort,
                                    PasswordEncoder passwordEncoder,
                                    JwtTokenProvider jwtTokenProvider) {
        this.usuarioRepositoryPort = usuarioRepositoryPort;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public String executar(String email, String senha, boolean jwtValidityRememberMe) {
        Usuario usuario = usuarioRepositoryPort.buscarPorEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Usuário ou senha inválidos."));

        if (!usuario.isAtivo() || usuario.isDeletado()) {
            throw new IllegalStateException("Usuário inativo ou desativado.");
        }

        if (!passwordEncoder.matches(senha, usuario.getSenha())) {
            throw new IllegalArgumentException("Usuário ou senha inválidos.");
        }

        return jwtTokenProvider.gerarToken(usuario.getEmail(), usuario.getPerfil().getNome(), jwtValidityRememberMe);
    }
}