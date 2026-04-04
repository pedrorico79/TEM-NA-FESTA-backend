package com.temnafesta.controller;

import com.temnafesta.dto.cardapioproduto.CardapioProdutoRequestDto;
import com.temnafesta.dto.cardapioproduto.CardapioProdutoResponseDto;
import com.temnafesta.mapper.CardapioProdutoMapper;
import com.temnafesta.model.CardapioProduto;
import com.temnafesta.service.CardapioProdutoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cardapio-produto")
public class CardapioProdutoController {

    private final CardapioProdutoService service;

    public CardapioProdutoController(CardapioProdutoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<CardapioProdutoResponseDto> criar(@RequestBody @Valid CardapioProdutoRequestDto dto) {
        CardapioProduto cardapioProduto = CardapioProdutoMapper.toEntity(dto);
        CardapioProduto criado = service.criar(cardapioProduto, dto.getCardapioId(), dto.getProdutoId());
        return ResponseEntity.status(201).body(CardapioProdutoMapper.toResponseDto(criado));
    }

    @GetMapping
    public ResponseEntity<List<CardapioProdutoResponseDto>> listar() {
        List<CardapioProduto> cardapioProdutos = service.listar();
        return ResponseEntity.ok(CardapioProdutoMapper.toResponseDtoList(cardapioProdutos));
    }

    @GetMapping("/cardapio/{cardapioId}")
    public ResponseEntity<List<CardapioProdutoResponseDto>> listarPorCardapio(@PathVariable Integer cardapioId) {
        List<CardapioProduto> cardapioProdutos = service.listarPorCardapio(cardapioId);
        return ResponseEntity.ok(CardapioProdutoMapper.toResponseDtoList(cardapioProdutos));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CardapioProdutoResponseDto> buscarPorId(@PathVariable Integer id) {
        CardapioProduto cardapioProduto = service.buscarPorId(id);
        return ResponseEntity.ok(CardapioProdutoMapper.toResponseDto(cardapioProduto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CardapioProdutoResponseDto> atualizar(@PathVariable Integer id, @RequestBody @Valid CardapioProdutoRequestDto dto) {
        CardapioProduto cardapioProduto = CardapioProdutoMapper.toEntity(dto);
        CardapioProduto atualizado = service.atualizar(id, cardapioProduto, dto.getCardapioId(), dto.getProdutoId());
        return ResponseEntity.ok(CardapioProdutoMapper.toResponseDto(atualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}