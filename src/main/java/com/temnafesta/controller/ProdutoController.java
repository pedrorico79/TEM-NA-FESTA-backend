package com.temnafesta.controller;


import com.temnafesta.dto.produto.ProdutoRequestDto;
import com.temnafesta.dto.produto.ProdutoResponseDto;
import com.temnafesta.mapper.ProdutoMapper;
import com.temnafesta.model.Produto;
import com.temnafesta.service.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {
    private final ProdutoService service;

    public ProdutoController(ProdutoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ProdutoResponseDto> criar(@RequestBody @Valid ProdutoRequestDto dto) {

        Produto produto = ProdutoMapper.toEntity(dto);
        Produto criado = service.criar(produto);

        return ResponseEntity.status(201)
                .body(ProdutoMapper.toResponseDto(criado));
    }

    @GetMapping
    public ResponseEntity<List<ProdutoResponseDto>> listar(
            @RequestParam(required = false, defaultValue = "true") Boolean apenasAtivos
    ) {
        List<Produto> produtos;
        if (apenasAtivos) {
            produtos = service.listarAtivos();
        } else {
            produtos = service.listarTodos();
        }

        return ResponseEntity.ok(
                ProdutoMapper.toResponseDtoList(produtos)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProdutoResponseDto> buscar(@PathVariable Integer id) {
        return ResponseEntity.ok(
                ProdutoMapper.toResponseDto(service.buscarPorId(id))
        );
    }

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

    @PatchMapping("/{id}/desativar")
    public ResponseEntity<Void> desativar(@PathVariable Integer id) {
        service.desativar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/reativar")
    public ResponseEntity<Void> reativar(@PathVariable Integer id) {
        service.reativar(id);
        return ResponseEntity.noContent().build();
    }
}
