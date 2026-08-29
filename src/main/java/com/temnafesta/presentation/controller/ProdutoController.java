package com.temnafesta.presentation.controller;

import com.temnafesta.application.dto.CriarProdutoCommand;
import com.temnafesta.application.usecase.CriarProdutoUseCase;
import com.temnafesta.application.usecase.ListarProdutosUseCase;
import com.temnafesta.domain.model.Produto;
import com.temnafesta.presentation.dto.CriarProdutoRequestDto;
import com.temnafesta.presentation.dto.ProdutoResponseDto;
import com.temnafesta.presentation.mapper.ProdutoPresentationMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/produtos")
@Tag(name = "Produto", description = "Endpoints para gerenciamento dos produtos")
public class ProdutoController {

    private final ListarProdutosUseCase listarProdutosUseCase;
    private final CriarProdutoUseCase criarProdutoUseCase;
    private final ProdutoPresentationMapper mapper;

    public ProdutoController(ListarProdutosUseCase listarProdutosUseCase,
                             CriarProdutoUseCase criarProdutoUseCase,
                             ProdutoPresentationMapper mapper) {
        this.listarProdutosUseCase = listarProdutosUseCase;
        this.criarProdutoUseCase = criarProdutoUseCase;
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

    @PostMapping
    @Operation(summary = "Cadastra um novo produto")
    @ApiResponse(responseCode = "201", description = "Produto cadastrado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos")
    @ApiResponse(responseCode = "422", description = "Regra de negócio violada")
    public ResponseEntity<ProdutoResponseDto> criar(
            @Valid @RequestBody CriarProdutoRequestDto request) {
        CriarProdutoCommand command = mapper.toCommand(request);
        Produto produtoSalvo = criarProdutoUseCase.executar(command);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(mapper.toResponse(produtoSalvo));
    }
}
