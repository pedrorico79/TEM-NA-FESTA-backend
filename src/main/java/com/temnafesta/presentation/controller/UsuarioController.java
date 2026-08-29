package com.temnafesta.presentation.controller;

import com.temnafesta.application.dto.CriarUsuarioCommand;
import com.temnafesta.application.usecase.AtualizarUsuarioUseCase;
import com.temnafesta.application.usecase.CriarUsuarioUseCase;
import com.temnafesta.application.usecase.DeletarUsuarioUseCase;
import com.temnafesta.application.usecase.ListarUsuarioUseCase;
import com.temnafesta.domain.model.Usuario;
import com.temnafesta.presentation.dto.AtualizarUsuarioRequestDto;
import com.temnafesta.presentation.dto.CriarUsuarioRequestDto;
import com.temnafesta.presentation.dto.UsuarioResponseDto;
import com.temnafesta.presentation.mapper.UsuarioPresentationMapper;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/usuarios")
public class UsuarioController {

    private final ListarUsuarioUseCase listarUsuarioUseCase;
    private final CriarUsuarioUseCase criarUsuarioUseCase;
    private final AtualizarUsuarioUseCase atualizarUsuarioUseCase;
    private final DeletarUsuarioUseCase deletarUsuarioUseCase;
    private final UsuarioPresentationMapper mapper;

    public UsuarioController(ListarUsuarioUseCase listarUsuarioUseCase,
                            CriarUsuarioUseCase criarUsuarioUseCase,
                            AtualizarUsuarioUseCase atualizarUsuarioUseCase,
                            DeletarUsuarioUseCase deletarUsuarioUseCase,
                            UsuarioPresentationMapper mapper) {
        this.listarUsuarioUseCase = listarUsuarioUseCase;
        this.criarUsuarioUseCase = criarUsuarioUseCase;
        this.atualizarUsuarioUseCase = atualizarUsuarioUseCase;
        this.deletarUsuarioUseCase = deletarUsuarioUseCase;
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
