package com.temnafesta.presentation.controller;

import com.temnafesta.application.dto.CriarLembreteCommand;
import com.temnafesta.application.usecase.CriarLembreteUseCase;
import com.temnafesta.application.usecase.ListarLembretesUsuarioUseCase;
import com.temnafesta.domain.model.Lembrete;
import com.temnafesta.infrastructure.security.user.UsuarioAutenticado;
import com.temnafesta.presentation.dto.LembreteRequestDto;
import com.temnafesta.presentation.dto.LembreteResponseDto;
import com.temnafesta.presentation.mapper.ConsultasPresentationMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/lembretes")
@Tag(name = "Lembretes", description = "Gerenciamento de lembretes internos dos usuários")
public class LembreteController {

    private final CriarLembreteUseCase criarLembreteUseCase;
    private final ListarLembretesUsuarioUseCase listarLembretesUsuarioUseCase;
    private final ConsultasPresentationMapper mapper;

    public LembreteController(CriarLembreteUseCase criarLembreteUseCase,
                              ListarLembretesUsuarioUseCase listarLembretesUsuarioUseCase,
                              ConsultasPresentationMapper mapper) {
        this.criarLembreteUseCase = criarLembreteUseCase;
        this.listarLembretesUsuarioUseCase = listarLembretesUsuarioUseCase;
        this.mapper = mapper;
    }

    @PostMapping
    @Operation(summary = "Cria um novo lembrete para um usuário")
    public ResponseEntity<LembreteResponseDto> criar(
            @Valid @RequestBody LembreteRequestDto request,
            @AuthenticationPrincipal UsuarioAutenticado usuarioAutenticado) {
        CriarLembreteCommand command = mapper.toCommand(request, usuarioAutenticado.getId());
        Lembrete lembreteSalvo = criarLembreteUseCase.executar(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(lembreteSalvo));
    }

    @GetMapping("/usuarios/{usuarioId}")
    @Operation(summary = "Lista todos os lembretes vinculados a um usuário específico")
    public ResponseEntity<List<LembreteResponseDto>> listarPorUsuario(@AuthenticationPrincipal UsuarioAutenticado usuarioAutenticado) {
        List<LembreteResponseDto> response = listarLembretesUsuarioUseCase.executar(usuarioAutenticado.getId())
                .stream()
                .map(mapper::toResponse)
                .toList();
        return ResponseEntity.ok(response);
    }
}