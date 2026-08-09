package com.temnafesta.controller;

import com.temnafesta.dto.lembrete.LembreteRequestDto;
import com.temnafesta.dto.lembrete.LembreteResponseDto;
import com.temnafesta.dto.usuario.UsuarioDetalhesDto;
import com.temnafesta.mapper.LembreteMapper;
import com.temnafesta.model.Lembrete;
import com.temnafesta.service.LembreteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/lembretes")
public class LembreteController {

    private final LembreteService lembreteService;

    public LembreteController(LembreteService lembreteService) {
        this.lembreteService = lembreteService;
    }

    @Operation(summary = "Cria um novo lembrete")
    @ApiResponse(responseCode = "201", description = "Lembrete criado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    @PostMapping
    public ResponseEntity<LembreteResponseDto> criar(
            @RequestBody @Valid LembreteRequestDto dto,
            @AuthenticationPrincipal UsuarioDetalhesDto usuarioLogado   // <-- vem do JWT validado
    ) {
        Lembrete novo = lembreteService.criar(LembreteMapper.toEntity(dto), usuarioLogado.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(LembreteMapper.toResponse(novo));
    }


    // Atualizar
    @Operation(summary = "Atualiza um lembrete existente")
    @ApiResponse(responseCode = "200", description = "Lembrete atualizado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    @ApiResponse(responseCode = "404", description = "Lembrete não encontrado")
    @PutMapping("/{id}")
    public ResponseEntity<LembreteResponseDto> atualizar(
            @PathVariable Integer id,
            @RequestBody @Valid LembreteRequestDto dto,
            @RequestParam Integer usuarioId
    ) {

        Lembrete atualizado = lembreteService.atualizar(
                id,
                LembreteMapper.toEntity(dto),
                usuarioId
        );

        return ResponseEntity.ok(
                LembreteMapper.toResponse(atualizado)
        );
    }


    // Listar todos
    @Operation(summary = "Lista todos os lembretes")
    @ApiResponse(responseCode = "200", description = "Listagem realizada com sucesso")
    @GetMapping("/meus")
    public ResponseEntity<List<LembreteResponseDto>> listarMeusLembretes(
            @AuthenticationPrincipal UsuarioDetalhesDto usuarioLogado
    ) {
        List<Lembrete> lembretes = lembreteService.listarPorUsuario(usuarioLogado.getId());
        return ResponseEntity.ok(LembreteMapper.toResponseList(lembretes));
    }


    // Buscar por ID
    @Operation(summary = "Busca lembrete por ID")
    @ApiResponse(responseCode = "200", description = "Lembrete encontrado com sucesso")
    @ApiResponse(responseCode = "404", description = "Lembrete não encontrado")
    @GetMapping("/{id}")
    public ResponseEntity<LembreteResponseDto> buscarPorId(@PathVariable Integer id) {
        Lembrete lembrete = lembreteService.buscarPorId(id);
        return ResponseEntity.ok(
                LembreteMapper.toResponse(lembrete)
        );
    }

    // Deletar
    @Operation(summary = "Remove um lembrete")
    @ApiResponse(responseCode = "204", description = "Lembrete removido com sucesso")
    @ApiResponse(responseCode = "404", description = "Lembrete não encontrado")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        lembreteService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    // Lembrete por usuário
    @Operation(summary = "Lista lembretes de um usuário")
    @ApiResponse(responseCode = "200", description = "Listagem realizada com sucesso")
    @GetMapping("/usuario/{usuarioId}")
    @PreAuthorize("hasRole('ADMIN')")   // só ADMIN pode ver lembretes de terceiros
    public ResponseEntity<List<LembreteResponseDto>> listarPorUsuario(@PathVariable Integer usuarioId) {
        List<Lembrete> lembretes = lembreteService.listarPorUsuario(usuarioId);
        return ResponseEntity.ok(LembreteMapper.toResponseList(lembretes));
    }

}
