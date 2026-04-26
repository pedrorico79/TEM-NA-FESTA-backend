package com.temnafesta.controller;

import com.temnafesta.dto.campanha.CampanhaRequestDto;
import com.temnafesta.dto.campanha.CampanhaResponseDto;
import com.temnafesta.mapper.CampanhaMapper;
import com.temnafesta.service.CampanhaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/campanhas")
@Tag(name = "Campanhas", description = "Campanhas promocionais vinculadas a cardápios")
public class CampanhaController {

    private final CampanhaService service;

    public CampanhaController(CampanhaService service) {
        this.service = service;
    }

    @Operation(summary = "Lista todas as campanhas")
    @ApiResponse(responseCode = "200", description = "Listagem realizada com sucesso")
    @ApiResponse(responseCode = "204", description = "Nenhuma campanha encontrada")
    @GetMapping
    public ResponseEntity<List<CampanhaResponseDto>> listarTudo(
            @RequestParam(required = false, defaultValue = "true") Boolean apenasAtivas
    ){
        List<CampanhaResponseDto> campanhas;
        if (apenasAtivas){
            campanhas = CampanhaMapper.toResponseDto(service.listarAtivas());
        } else {
            campanhas = CampanhaMapper.toResponseDto(service.listarTodas());
        }

        if (campanhas.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(campanhas);
    }

    @Operation(summary = "Busca campanha por ID")
    @ApiResponse(responseCode = "200", description = "Campanha encontrada com sucesso")
    @ApiResponse(responseCode = "404", description = "Campanha não encontrada")
    @GetMapping("/{id}")
    public ResponseEntity<CampanhaResponseDto> ProcurarPorId(@PathVariable Integer id){
        return ResponseEntity.ok(CampanhaMapper.toResponse(service.findById(id)));
    }

    @Operation(summary = "Cria uma nova campanha")
    @ApiResponse(responseCode = "201", description = "Campanha criada com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    @PostMapping
    public ResponseEntity<CampanhaResponseDto> create(@RequestBody @Valid CampanhaRequestDto dto){
        CampanhaResponseDto created = CampanhaMapper.toResponse(service.create(CampanhaMapper.toEntityForCreate(dto)));
        URI location = URI.create("/campanhas/" + created.getId());
        return ResponseEntity.created(location).body(created);
    }

    @Operation(summary = "Atualiza uma campanha existente")
    @ApiResponse(responseCode = "200", description = "Campanha atualizada com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    @ApiResponse(responseCode = "404", description = "Campanha não encontrada")
    @PutMapping("/{id}")
    public ResponseEntity<CampanhaResponseDto> update(@PathVariable Integer id,
                                                      @RequestBody
                                                      @Valid CampanhaRequestDto dto){
        return ResponseEntity.ok(CampanhaMapper.toResponse(service.update(id, CampanhaMapper.toEntityForUpdate(dto))));
    }

    @Operation(summary = "Desativa uma campanha")
    @ApiResponse(responseCode = "204", description = "Campanha desativada com sucesso")
    @ApiResponse(responseCode = "404", description = "Campanha não encontrada")
    @PatchMapping("/{id}/desativar")
    public ResponseEntity<Void> desativar(@PathVariable Integer id){
        service.desativar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Reativa uma campanha")
    @ApiResponse(responseCode = "204", description = "Campanha reativada com sucesso")
    @ApiResponse(responseCode = "404", description = "Campanha não encontrada")
    @PatchMapping("/{id}/reativar")
    public ResponseEntity<Void> reativar(@PathVariable Integer id){
        service.reativar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Lista campanhas inativas")
    @ApiResponse(responseCode = "200", description = "Listagem realizada com sucesso")
    @ApiResponse(responseCode = "204", description = "Nenhuma campanha inativa encontrada")
    @GetMapping("/inativas")
    public ResponseEntity<List<CampanhaResponseDto>> listarInativas() {
        List<CampanhaResponseDto> campanhas = CampanhaMapper.toResponseDto(service.listarInativas());
        if(campanhas.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(campanhas);
    }
}
