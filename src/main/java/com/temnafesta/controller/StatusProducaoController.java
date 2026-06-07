package com.temnafesta.controller;

import com.temnafesta.dto.statusProducao.StatusProducaoResponseDto;
import com.temnafesta.service.StatusProducaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/status-producao")
@Tag(
        name = "Status Produção",
        description = "Consulta dos status de produção"
)
public class StatusProducaoController {

    private final StatusProducaoService service;

    public StatusProducaoController(
            StatusProducaoService service
    ) {
        this.service = service;
    }

    @Operation(summary = "Lista todos os status de produção")
    @ApiResponse(
            responseCode = "200",
            description = "Status encontrados com sucesso"
    )
    @ApiResponse(
            responseCode = "204",
            description = "Nenhum status encontrado"
    )
    @GetMapping
    public ResponseEntity<List<StatusProducaoResponseDto>>
    listarTodos() {

        List<StatusProducaoResponseDto> status =
                service.listarTodos();

        if (status.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(status);
    }
}