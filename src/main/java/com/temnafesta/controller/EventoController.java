package com.temnafesta.controller;

import com.temnafesta.dto.evento.EventoRequestDto;
import com.temnafesta.dto.evento.EventoResponseDto;
import com.temnafesta.mapper.EventoMapper;
import com.temnafesta.service.EventoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/eventos")
@Tag(name = "Eventos", description = "Eventos promocionais vinculadas a cardápios")
public class EventoController {

    private final EventoService service;

    public EventoController(EventoService service) {
        this.service = service;
    }

    @Operation(summary = "Lista todas os eventos")
    @ApiResponse(responseCode = "200", description = "Listagem realizada com sucesso")
    @ApiResponse(responseCode = "204", description = "Nenhum evento encontrado")
    @GetMapping
    public ResponseEntity<List<EventoResponseDto>> listarTudo(
            @RequestParam(required = false, defaultValue = "true") Boolean apenasAtivas
    ){
        List<EventoResponseDto> eventos;
        if (apenasAtivas){
            eventos = EventoMapper.toResponseDto(service.listarAtivos());
        } else {
            eventos = EventoMapper.toResponseDto(service.listarTodos());
        }

        if (eventos.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(eventos);
    }

//    @Operation(summary = "Busca evento por ID")
//    @ApiResponse(responseCode = "200", description = "Evento encontrada com sucesso")
//    @ApiResponse(responseCode = "404", description = "Evento não encontrada")
//    @GetMapping("/{id}")
//    public ResponseEntity<EventoResponseDto> ProcurarPorId(@PathVariable Integer id){
//        return ResponseEntity.ok(EventoMapper.toResponse(service.buscarPorId(id)));
//    }

    @Operation(summary = "Cria um novo evento")
    @ApiResponse(responseCode = "201", description = "Evento criada com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    @PostMapping
    public ResponseEntity<EventoResponseDto> create(@RequestBody @Valid EventoRequestDto dto){
        EventoResponseDto created = EventoMapper.toResponse(service.criar(EventoMapper.toEntityForCreate(dto)));
        URI location = URI.create("/eventos/" + created.getId());
        return ResponseEntity.created(location).body(created);
    }

    @Operation(summary = "Atualiza um evento existente")
    @ApiResponse(responseCode = "200", description = "Evento atualizada com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    @ApiResponse(responseCode = "404", description = "Evento não encontrada")
    @PutMapping("/{id}")
    public ResponseEntity<EventoResponseDto> atualizar(
            @PathVariable Integer id,
            @RequestBody @Valid EventoRequestDto dto
    ) {
        return ResponseEntity.ok(
                EventoMapper.toResponse(
                        service.atualizar(
                                id,
                                EventoMapper.toEntityForUpdate(dto))
                ));
    }


//    // ======================================== \/ \/ ========================================
//    @Operation(summary = "Desativa um evento")
//    @ApiResponse(responseCode = "204", description = "Evento desativado com sucesso")
//    @ApiResponse(responseCode = "404", description = "Evento não encontrada")
//    @PatchMapping("/{id}/desativar")
//    public ResponseEntity<Void> desativar(@PathVariable Integer id){
//        service.desativar(id);
//        return ResponseEntity.noContent().build();
//    }
//
//    // ======================================== TUDO ISSO AQUI DEVE VIRAR APENAS 1 METODO COM PATCH
//    // PATCH eventos/{id} com body de campo is_ativo
//
//    @Operation(summary = "Reativa um evento")
//    @ApiResponse(responseCode = "204", description = "Evento reativado com sucesso")
//    @ApiResponse(responseCode = "404", description = "Evento não encontrado")
//    @PatchMapping("/{id}/reativar")
//    public ResponseEntity<Void> reativar(@PathVariable Integer id){
//        service.reativar(id);
//        return ResponseEntity.noContent().build();
//    }
//    // ======================================== /\ /\ ========================================

    @Operation(summary = "Ativa ou desativa um evento")
    @ApiResponse(responseCode = "204", description = "Status atualizado com sucesso")
    @ApiResponse(responseCode = "404", description = "Evento não encontrado")
    @PatchMapping("/{id}/ativo")
    public ResponseEntity<Void> toggleAtivo(@PathVariable Integer id) {
        service.toggleAtivo(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Remove um evento")
    @ApiResponse(responseCode = "204", description = "Evento removido com sucesso")
    @ApiResponse(responseCode = "404", description = "Evento não encontrado")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }


//    @Operation(summary = "Lista eventos inativos")
//    @ApiResponse(responseCode = "200", description = "Listagem realizada com sucesso")
//    @ApiResponse(responseCode = "204", description = "Nenhum evento inativa encontrada")
//    @GetMapping("/inativas")
//    public ResponseEntity<List<EventoResponseDto>> listarInativas() {
//        List<EventoResponseDto> eventos = EventoMapper.toResponseDto(
//                service.listarInativas());
//        if(eventos.isEmpty()) return ResponseEntity.noContent().build();
//        return ResponseEntity.ok(eventos);
//    }
}
