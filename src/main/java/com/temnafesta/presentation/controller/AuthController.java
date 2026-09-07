package com.temnafesta.presentation.controller;

import com.temnafesta.application.usecase.AutenticarUsuarioUseCase;
import com.temnafesta.infrastructure.security.user.UsuarioAutenticado;
import com.temnafesta.presentation.dto.LoginRequestDto;
import com.temnafesta.presentation.dto.MeResponseDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.temnafesta.infrastructure.security.jwt.JwtTokenProvider;

import java.time.Duration;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AutenticarUsuarioUseCase autenticarUsuarioUseCase;
    private final JwtTokenProvider jwtTokenProvider;

    @Value("${app.jwt.cookie-name:auth_token}")
    private String jwtCookieName;

    @Value("${app.jwt.cookie-path:/}")
    private String jwtCookiePath;

    @Value("${app.jwt.cookie-secure:false}")
    private boolean jwtCookieSecure;

    @Value("${app.jwt.cookie-same-site:Lax}")
    private String jwtCookieSameSite;

    @Value("${app.jwt.cookie-domain:}")
    private String jwtCookieDomain;

    public AuthController(AutenticarUsuarioUseCase autenticarUsuarioUseCase, JwtTokenProvider jwtTokenProvider) {
        this.autenticarUsuarioUseCase = autenticarUsuarioUseCase;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@Valid @RequestBody LoginRequestDto request) {
        boolean jwtValidityRememberMe = Boolean.TRUE.equals(request.jwtValidityRememberMe());
        String token = autenticarUsuarioUseCase.executar(request.email(), request.senha(), jwtValidityRememberMe);

        ResponseCookie.ResponseCookieBuilder cookieBuilder = ResponseCookie.from(jwtCookieName, token)
                .httpOnly(true)
                .secure(jwtCookieSecure)
                .path(jwtCookiePath)
                .sameSite(jwtCookieSameSite)
                .maxAge(Duration.ofMillis(jwtTokenProvider.getExpiracaoMs(jwtValidityRememberMe)));

        if (jwtCookieDomain != null && !jwtCookieDomain.isBlank()) {
            cookieBuilder.domain(jwtCookieDomain);
        }

        return ResponseEntity.noContent()
                .header(HttpHeaders.SET_COOKIE, cookieBuilder.build().toString())
                .build();
    }

    @GetMapping("/me")
    public ResponseEntity<MeResponseDto> me(@AuthenticationPrincipal UsuarioAutenticado usuario) {
        return ResponseEntity.ok(MeResponseDto.from(usuario));
    }
}
