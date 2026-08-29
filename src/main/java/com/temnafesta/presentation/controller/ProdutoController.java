package com.temnafesta.presentation.controller;

import com.temnafesta.application.usecase.ListarProdutosUseCase;
import com.temnafesta.presentation.dto.ProdutoResponseDto;
import com.temnafesta.presentation.mapper.ProdutoPresentationMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/produtos")
@Tag(name = "Produto", description = "Endpoints para consulta dos produtos")
public class ProdutoController {

    private final ListarProdutosUseCase listarProdutosUseCase;
    private final ProdutoPresentationMapper mapper;

    public ProdutoController(ListarProdutosUseCase listarProdutosUseCase,
                             ProdutoPresentationMapper mapper) {
        this.listarProdutosUseCase = listarProdutosUseCase;
        this.mapper = mapper;
    }

    @GetMapping
    @Operation(summary = "Lista os produtos não deletados, com filtro opcional por nome e ativos primeiro")
    public ResponseEntity<List<ProdutoResponseDto>> listar(
            @RequestParam(required = false) String nome) {
        List<ProdutoResponseDto> response = listarProdutosUseCase.executar(nome)
                .stream()
                .map(mapper::toResponse)
                .toList();

        return ResponseEntity.ok(response);
    }
}
