package com.temnafesta.presentation.controller;

import com.temnafesta.application.usecase.ListarStatusProducaoUseCase;
import com.temnafesta.domain.vo.StatusProducaoEnum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/status")
@Tag(name = "Status", description = "Consulta dos status de produção existentes")
public class StatusProducaoController {

    private final ListarStatusProducaoUseCase listarStatusProducaoUseCase;

    public StatusProducaoController(ListarStatusProducaoUseCase listarStatusProducaoUseCase) {
        this.listarStatusProducaoUseCase = listarStatusProducaoUseCase;
    }

    @GetMapping
    @Operation(summary = "Lista todos os status existentes para servir de filtro")
    public ResponseEntity<List<StatusProducaoEnum>> listar() {
        return ResponseEntity.ok(listarStatusProducaoUseCase.executar());
    }
}
