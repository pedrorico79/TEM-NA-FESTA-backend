package com.temnafesta.presentation.controller;

import com.temnafesta.application.dto.CriarUsuarioCommand;
import com.temnafesta.application.usecase.AtualizarUsuarioUseCase;
import com.temnafesta.application.usecase.CriarUsuarioUseCase;
import com.temnafesta.application.usecase.DeletarUsuarioUseCase;
import com.temnafesta.application.usecase.ListarUsuarioUseCase;
import com.temnafesta.domain.model.Usuario;
import com.temnafesta.presentation.dto.CriarUsuarioRequestDto;
import com.temnafesta.presentation.mapper.UsuarioPresentationMapper;
import com.temnafesta.presentation.dto.UsuarioResponseDto;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/v1/usuarios")
public class UsuarioController {

    private final ListarUsuarioUseCase listarUsuarioUseCase;
    private final CriarUsuarioUseCase criarUsuarioUseCase;
    private final AtualizarUsuarioUseCase atualizarUsuarioUseCase;
    private final DeletarUsuarioUseCase deletarUsuarioUseCase;
    private final UsuarioPresentationMapper mapper;

    public UsuarioController(ListarUsuarioUseCase listarUsuarioUseCase, CriarUsuarioUseCase criarUsuarioUseCase, AtualizarUsuarioUseCase atualizarUsuarioUseCase, DeletarUsuarioUseCase deletarUsuarioUseCase, UsuarioPresentationMapper mapper) {
        this.listarUsuarioUseCase = listarUsuarioUseCase;
        this.criarUsuarioUseCase = criarUsuarioUseCase;
        this.atualizarUsuarioUseCase = atualizarUsuarioUseCase;
        this.deletarUsuarioUseCase = deletarUsuarioUseCase;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<UsuarioResponseDto> criarUsuario(@Valid @RequestBody CriarUsuarioRequestDto request) {
        CriarUsuarioCommand command = mapper.toCommand(request);

        Usuario usuarioSalvo = criarUsuarioUseCase.executar(command);
        UsuarioResponseDto response = mapper.toResponse(usuarioSalvo);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


}
