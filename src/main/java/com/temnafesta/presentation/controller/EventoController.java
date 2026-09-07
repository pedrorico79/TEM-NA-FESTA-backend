package com.temnafesta.presentation.controller;

import com.temnafesta.application.dto.AtualizarEventoCommand;
import com.temnafesta.application.dto.CriarEventoCommand;
import com.temnafesta.application.usecase.AlterarStatusEventoUseCase;
import com.temnafesta.application.usecase.AtualizarEventoUseCase;
import com.temnafesta.application.usecase.CriarEventoUseCase;
import com.temnafesta.application.usecase.DeletarEventoUseCase;
import com.temnafesta.application.usecase.ListarEventosAtivosUseCase;
import com.temnafesta.domain.model.Evento;
import com.temnafesta.presentation.dto.AlterarStatusEventoRequestDto;
import com.temnafesta.presentation.dto.AtualizarEventoRequestDto;
import com.temnafesta.presentation.dto.CriarEventoRequestDto;
import com.temnafesta.presentation.dto.EventoResponseDto;
import com.temnafesta.presentation.mapper.ConsultasPresentationMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/eventos")
@Tag(name = "Eventos", description = "Endpoints para gestão de eventos")
public class EventoController {

    private final ListarEventosAtivosUseCase listarEventosAtivosUseCase;
    private final CriarEventoUseCase criarEventoUseCase;
    private final AtualizarEventoUseCase atualizarEventoUseCase;
    private final DeletarEventoUseCase deletarEventoUseCase;
    private final AlterarStatusEventoUseCase alterarStatusEventoUseCase;
    private final ConsultasPresentationMapper mapper;

    public EventoController(ListarEventosAtivosUseCase listarEventosAtivosUseCase,
                           CriarEventoUseCase criarEventoUseCase,
                           AtualizarEventoUseCase atualizarEventoUseCase,
                           DeletarEventoUseCase deletarEventoUseCase,
                           AlterarStatusEventoUseCase alterarStatusEventoUseCase,
                           ConsultasPresentationMapper mapper) {
        this.listarEventosAtivosUseCase = listarEventosAtivosUseCase;
        this.criarEventoUseCase = criarEventoUseCase;
        this.atualizarEventoUseCase = atualizarEventoUseCase;
        this.deletarEventoUseCase = deletarEventoUseCase;
        this.alterarStatusEventoUseCase = alterarStatusEventoUseCase;
        this.mapper = mapper;
    }

    @GetMapping
    @Operation(summary = "Lista todos os eventos ativos e não deletados")
    public ResponseEntity<List<EventoResponseDto>> listarAtivos() {
        List<EventoResponseDto> response = listarEventosAtivosUseCase.executar()
                .stream()
                .map(mapper::toResponse)
                .toList();
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "Cadastra um novo evento")
    public ResponseEntity<EventoResponseDto> criarEvento(@Valid @RequestBody CriarEventoRequestDto request) {
        CriarEventoCommand command = new CriarEventoCommand(request.nome(), request.dataInicio(), request.dataFim());
        Evento eventoSalvo = criarEventoUseCase.executar(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(eventoSalvo));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza um evento existente")
    public ResponseEntity<EventoResponseDto> atualizarEvento(@PathVariable Long id,
                                                           @Valid @RequestBody AtualizarEventoRequestDto request) {
        AtualizarEventoCommand command = new AtualizarEventoCommand(request.nome(), request.dataInicio(), request.dataFim());
        Evento eventoAtualizado = atualizarEventoUseCase.executar(id, command);
        return ResponseEntity.ok(mapper.toResponse(eventoAtualizado));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Realiza soft delete de um evento")
    public ResponseEntity<Void> deletarEvento(@PathVariable Long id) {
        deletarEventoUseCase.executar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Ativa ou desativa um evento")
    public ResponseEntity<EventoResponseDto> alterarStatusEvento(@PathVariable Long id,
                                                                @Valid @RequestBody AlterarStatusEventoRequestDto request) {
        Evento evento = alterarStatusEventoUseCase.executar(id, Boolean.TRUE.equals(request.ativo()));
        return ResponseEntity.ok(mapper.toResponse(evento));
    }
}