package com.temnafesta.presentation.controller;

import com.temnafesta.application.usecase.ListarEventosAtivosUseCase;
import com.temnafesta.presentation.dto.EventoResponseDto;
import com.temnafesta.presentation.mapper.ConsultasPresentationMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/eventos")
@Tag(name = "Eventos", description = "Endpoints para consulta de eventos sazonais")
public class EventoController {

    private final ListarEventosAtivosUseCase listarEventosAtivosUseCase;
    private final ConsultasPresentationMapper mapper;

    public EventoController(ListarEventosAtivosUseCase listarEventosAtivosUseCase, ConsultasPresentationMapper mapper) {
        this.listarEventosAtivosUseCase = listarEventosAtivosUseCase;
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
}