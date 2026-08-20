package com.temnafesta.presentation.controller;

import com.temnafesta.application.usecase.ListarMetodosPagamentoUseCase;
import com.temnafesta.presentation.dto.MetodoPagamentoResponseDto;
import com.temnafesta.presentation.mapper.ConsultasPresentationMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/metodos-pagamento")
@Tag(name = "Métodos de Pagamento", description = "Endpoints para consulta dos métodos de recebimento")
public class MetodoPagamentoController {

    private final ListarMetodosPagamentoUseCase listarMetodosPagamentoUseCase;
    private final ConsultasPresentationMapper mapper;

    public MetodoPagamentoController(ListarMetodosPagamentoUseCase listarMetodosPagamentoUseCase, ConsultasPresentationMapper mapper) {
        this.listarMetodosPagamentoUseCase = listarMetodosPagamentoUseCase;
        this.mapper = mapper;
    }

    @GetMapping
    @Operation(summary = "Lista todos os métodos de pagamento cadastrados")
    public ResponseEntity<List<MetodoPagamentoResponseDto>> listarTodos() {
        List<MetodoPagamentoResponseDto> response = listarMetodosPagamentoUseCase.executar()
                .stream()
                .map(mapper::toResponse)
                .toList();
        return ResponseEntity.ok(response);
    }
}