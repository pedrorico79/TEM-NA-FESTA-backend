package com.temnafesta.controller;

import com.temnafesta.dto.produto.ProdutoRequestDto;
import com.temnafesta.dto.produto.ProdutoResponseDto;
import com.temnafesta.mapper.ProdutoMapper;
import com.temnafesta.model.Produto;
import com.temnafesta.service.ProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/produtos")
@Tag(name = "Produtos", description = "Cadastro de produtos disponíveis")
public class ProdutoController {

    private final ProdutoService service;

    public ProdutoController(ProdutoService service) {
        this.service = service;
    }

    @Operation(summary = "Cria um novo produto")
    @ApiResponse(responseCode = "201", description = "Produto criado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    @PostMapping
    public ResponseEntity<ProdutoResponseDto> criar(@RequestBody @Valid ProdutoRequestDto dto) {

        Produto produto = ProdutoMapper.toEntity(dto);
        Produto criado = service.criar(produto);

        return ResponseEntity.status(201)
                .body(ProdutoMapper.toResponseDto(criado));
    }

    @Operation(summary = "Lista todos os produtos")
    @ApiResponse(responseCode = "200", description = "Listagem realizada com sucesso")
    @GetMapping
    public ResponseEntity<List<ProdutoResponseDto>> listar() {
        return ResponseEntity.ok(
                ProdutoMapper.toResponseDtoList(service.listar())
        );
    }

    @Operation(summary = "Busca produto por ID")
    @ApiResponse(responseCode = "200", description = "Produto encontrado com sucesso")
    @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    @GetMapping("/{id}")
    public ResponseEntity<ProdutoResponseDto> buscar(@PathVariable Integer id) {
        return ResponseEntity.ok(
                ProdutoMapper.toResponseDto(service.buscarPorId(id))
        );
    }

    @Operation(summary = "Atualiza um produto existente")
    @ApiResponse(responseCode = "200", description = "Produto atualizado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    @PutMapping("/{id}")
    public ResponseEntity<ProdutoResponseDto> atualizar(
            @PathVariable Integer id,
            @RequestBody @Valid ProdutoRequestDto dto) {

        Produto produto = ProdutoMapper.toEntity(dto);
        Produto atualizado = service.atualizar(id, produto);

        return ResponseEntity.ok(
                ProdutoMapper.toResponseDto(atualizado)
        );
    }

    @Operation(summary = "Remove um produto")
    @ApiResponse(responseCode = "204", description = "Produto removido com sucesso")
    @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}