package com.temnafesta.controller;

import com.temnafesta.dto.metodoPagamento.MetodoPagamentoResponseDto;
import com.temnafesta.service.MetodoPagamentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/metodos-pagamento")
@Tag(name = "Métodos de Pagamento", description = "Possíveis métodos de pagamento a serem usados")

public class MetodoPagamentoController {

    private final MetodoPagamentoService service;

    public MetodoPagamentoController(
            MetodoPagamentoService service
    ) {
        this.service = service;
    }

    @Operation(summary = "Lista todos os métodos de pagamento")
    @ApiResponse(responseCode = "200",
            description = "Métodos encontrados com sucesso")
    @ApiResponse(responseCode = "204",
            description = "Nenhum método encontrado")
    @GetMapping
    public ResponseEntity<List<MetodoPagamentoResponseDto>>
    listarTodos() {

        List<MetodoPagamentoResponseDto> metodos =
                service.listarTodos();

        if (metodos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(metodos);
    }
}