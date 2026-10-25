package com.temnafesta.application.usecase;

import com.temnafesta.application.exception.FalhaAutenticacaoException;
import com.temnafesta.domain.model.Usuario;
import com.temnafesta.domain.ports.out.repository.UsuarioRepositoryPort;
import com.temnafesta.infrastructure.security.jwt.JwtTokenProvider;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Locale;

public class AutenticarUsuarioUseCase {

    private static final String HASH_DUMMY_BCRYPT = "$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy";

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
        String emailNormalizado = email == null ? "" : email.trim().toLowerCase(Locale.ROOT);
        Usuario usuario = usuarioRepositoryPort.buscarPorEmail(emailNormalizado).orElse(null);
        String hashComparacao = usuario == null ? HASH_DUMMY_BCRYPT : usuario.getSenha();
        boolean senhaValida = passwordEncoder.matches(senha == null ? "" : senha, hashComparacao);

        if (usuario == null || !senhaValida || !usuario.isAtivo() || usuario.isDeletado()) {
            throw new FalhaAutenticacaoException();
        }

        return jwtTokenProvider.gerarToken(usuario.getEmail(), usuario.getPerfil().getNome(), jwtValidityRememberMe);
    }
}
