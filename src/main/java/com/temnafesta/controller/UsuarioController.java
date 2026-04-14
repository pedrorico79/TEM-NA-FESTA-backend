package com.temnafesta.controller;

import com.temnafesta.dto.usuario.*;
import com.temnafesta.mapper.UsuarioMapper;
import com.temnafesta.model.Usuario;
import com.temnafesta.service.UsuarioService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService service;

    @Value("${jwt.validity}")
    private long jwtValidity;

    public static final String COOKIE_NOME = "authToken";

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Void> criar(@RequestBody @Valid UsuarioCriacaoDto dto) {
        Usuario novoUsuario = UsuarioMapper.toEntity(dto);
        this.service.criar(novoUsuario);
        return ResponseEntity.status(201).build();
    }

    @PostMapping("/login")
    public ResponseEntity<UsuarioSessaoDto> login(
            @RequestBody @Valid UsuarioLoginDto loginDto,
            HttpServletResponse response) {

        // O service autentica e retorna o objeto com o Token JWT
        UsuarioTokenDto autenticado = this.service.autenticar(loginDto);

        // Configuração do Cookie HttpOnly para segurança XSS
        ResponseCookie cookie = ResponseCookie.from(COOKIE_NOME, autenticado.getToken())
                .httpOnly(true)
                .secure(false) // Mudar para true em produção (HTTPS)
                .sameSite("Strict")
                .path("/")
                .maxAge(Duration.ofSeconds(jwtValidity))
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        // Retorna os dados do usuário para o front, mas sem o token no JSON
        UsuarioSessaoDto sessao = new UsuarioSessaoDto();
        sessao.setUserId(autenticado.getUserId());
        sessao.setNome(autenticado.getNome());
        sessao.setEmail(autenticado.getEmail());

        return ResponseEntity.ok(sessao);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from(COOKIE_NOME, "")
                .httpOnly(true)
                .secure(false)
                .sameSite("Strict")
                .path("/")
                .maxAge(0) // Expira o cookie imediatamente
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<List<UsuarioListarDto>> listar(
            @RequestParam(required = false, defaultValue = "true") Boolean apenasAtivos
    ) {
        List<Usuario> usuarios;
        if (apenasAtivos) {
            usuarios = service.listarAtivos();
        } else {
            usuarios = service.listar();
        }

        if (usuarios.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(UsuarioMapper.toListarDtoList(usuarios));
    }

    @GetMapping("/{id}")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<UsuarioListarDto> buscarPorId(@PathVariable Integer id) {
        Usuario usuario = service.buscarPorId(id);
        return ResponseEntity.ok(UsuarioMapper.toListarDto(usuario));
    }

    @PutMapping("/{id}")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<UsuarioListarDto> atualizar(
            @PathVariable Integer id,
            @RequestBody @Valid UsuarioAtualizacaoDto dto) {

        Usuario dadosAtualizados = UsuarioMapper.toEntity(dto);
        Usuario usuarioSalvo = this.service.atualizar(id, dadosAtualizados);
        return ResponseEntity.ok(UsuarioMapper.toListarDto(usuarioSalvo));
    }

    @PatchMapping("/{id}/senha")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<Void> atualizarSenha(
            @PathVariable Integer id,
            @RequestBody @Valid UsuarioSenhaDto dto) {

        this.service.atualizarSenha(id, dto.getNovaSenha());

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/desativar")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<Void> desativar(@PathVariable Integer id) {
        service.desativar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/reativar")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<Void> reativar(@PathVariable Integer id) {
        service.reativar(id);
        return ResponseEntity.noContent().build();
    }
}