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
    public ResponseEntity<List<ProdutoResponseDto>> listar() {
        return ResponseEntity.ok(
                ProdutoMapper.toResponseDtoList(service.listar())
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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
