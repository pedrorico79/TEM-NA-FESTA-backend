package com.temnafesta.presentation.controller;

import com.temnafesta.application.usecase.AutenticarUsuarioUseCase;
import com.temnafesta.presentation.dto.LoginRequestDto;
import com.temnafesta.presentation.dto.TokenResponseDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AutenticarUsuarioUseCase autenticarUsuarioUseCase;

    public AuthController(AutenticarUsuarioUseCase autenticarUsuarioUseCase) {
        this.autenticarUsuarioUseCase = autenticarUsuarioUseCase;
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> login(@Valid @RequestBody LoginRequestDto request) {
        System.out.println("Email: " + request.email() + ", Senha: " + request.senha());
        System.out.println("Token gerado: " + autenticarUsuarioUseCase.executar(request.email(), request.senha()));
        String token = autenticarUsuarioUseCase.executar(request.email(), request.senha());
        return ResponseEntity.ok(new TokenResponseDto(token, "Bearer"));
    }
}
