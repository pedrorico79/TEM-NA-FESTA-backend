package com.temnafesta.presentation.controller;

import com.temnafesta.application.dto.CriarUsuarioCommand;
import com.temnafesta.application.usecase.AtualizarUsuarioUseCase;
import com.temnafesta.application.usecase.AutenticarUsuarioUseCase;
import com.temnafesta.application.usecase.CriarUsuarioUseCase;
import com.temnafesta.application.usecase.DeletarUsuarioUseCase;
import com.temnafesta.application.usecase.ListarUsuarioUseCase;
import com.temnafesta.domain.model.Usuario;
import com.temnafesta.infrastructure.security.jwt.JwtTokenProvider;
import com.temnafesta.infrastructure.security.user.UsuarioAutenticado;
import com.temnafesta.presentation.dto.*;
import com.temnafesta.presentation.mapper.UsuarioPresentationMapper;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
@RequestMapping("/api/v1/usuarios")
public class UsuarioController {

    private final ListarUsuarioUseCase listarUsuarioUseCase;
    private final CriarUsuarioUseCase criarUsuarioUseCase;
    private final AtualizarUsuarioUseCase atualizarUsuarioUseCase;
    private final DeletarUsuarioUseCase deletarUsuarioUseCase;
    private final AutenticarUsuarioUseCase autenticarUsuarioUseCase;
    private final JwtTokenProvider jwtTokenProvider;
    private final UsuarioPresentationMapper mapper;

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

    public UsuarioController(ListarUsuarioUseCase listarUsuarioUseCase,
                            CriarUsuarioUseCase criarUsuarioUseCase,
                            AtualizarUsuarioUseCase atualizarUsuarioUseCase,
                            DeletarUsuarioUseCase deletarUsuarioUseCase,
                            AutenticarUsuarioUseCase autenticarUsuarioUseCase,
                            JwtTokenProvider jwtTokenProvider,
                            UsuarioPresentationMapper mapper) {
        this.listarUsuarioUseCase = listarUsuarioUseCase;
        this.criarUsuarioUseCase = criarUsuarioUseCase;
        this.atualizarUsuarioUseCase = atualizarUsuarioUseCase;
        this.deletarUsuarioUseCase = deletarUsuarioUseCase;
        this.autenticarUsuarioUseCase = autenticarUsuarioUseCase;
        this.jwtTokenProvider = jwtTokenProvider;
        this.mapper = mapper;
    }

    @GetMapping
    public ResponseEntity<Page<UsuarioResponseDto>> listarUsuarios(
            @RequestParam(required = false) String nome,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<UsuarioResponseDto> response = listarUsuarioUseCase.executar(nome, pageable)
                .map(mapper::toResponse);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<UsuarioResponseDto> criarUsuario(@Valid @RequestBody CriarUsuarioRequestDto request) {
        CriarUsuarioCommand command = mapper.toCommand(request);
        Usuario usuarioSalvo = criarUsuarioUseCase.executar(command);
        UsuarioResponseDto response = mapper.toResponse(usuarioSalvo);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@Valid @RequestBody LoginRequestDto request, HttpServletResponse response) {
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

        response.addHeader(HttpHeaders.SET_COOKIE, cookieBuilder.build().toString());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    public ResponseEntity<MeResponseDto> me(@AuthenticationPrincipal UsuarioAutenticado usuario) {
        return ResponseEntity.ok(MeResponseDto.from(usuario));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from(jwtCookieName, "")
                .httpOnly(true)
                .secure(jwtCookieSecure)
                .path(jwtCookiePath)
                .sameSite(jwtCookieSameSite)
                .maxAge(0)
                .build();

        if (jwtCookieDomain != null && !jwtCookieDomain.isBlank()) {
            cookie = ResponseCookie.from(jwtCookieName, "")
                    .httpOnly(true)
                    .secure(jwtCookieSecure)
                    .path(jwtCookiePath)
                    .sameSite(jwtCookieSameSite)
                    .maxAge(0)
                    .domain(jwtCookieDomain)
                    .build();
        }

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDto> atualizarUsuario(@PathVariable Long id,
                                                             @Valid @RequestBody AtualizarUsuarioRequestDto request) {
        Usuario usuarioAtualizado = atualizarUsuarioUseCase.executar(id, request.nome(), request.email(), request.perfilId());
        return ResponseEntity.ok(mapper.toResponse(usuarioAtualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarUsuario(@PathVariable Long id) {
        deletarUsuarioUseCase.executar(id);
        return ResponseEntity.noContent().build();
    }
}
