package com.temnafesta.controller;

import com.temnafesta.dto.cardapio.CardapioAtivoDto;
import com.temnafesta.dto.cardapio.CardapioRequestDto;
import com.temnafesta.dto.cardapio.CardapioResponseDto;
import com.temnafesta.mapper.CardapioMapper;
import com.temnafesta.service.CardapioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/cardapios")
public class CardapioController {

    private final CardapioService service;

    public CardapioController(CardapioService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<CardapioResponseDto>> findAll() {
        List<CardapioResponseDto> cardapios = CardapioMapper.toResponseDto(service.findAll());
        if (cardapios.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(cardapios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CardapioResponseDto> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(CardapioMapper.toResponse(service.findById(id)));
    }

    @PostMapping
    public ResponseEntity<CardapioResponseDto> create(@RequestBody @Valid CardapioRequestDto dto) {
        CardapioResponseDto created = CardapioMapper.toResponse(service.create(dto));
        URI location = URI.create("/tem-na-festa/cardapios/" + created.getId());
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CardapioResponseDto> update(@PathVariable Integer id,
                                                      @RequestBody @Valid CardapioRequestDto dto) {
        return ResponseEntity.ok(CardapioMapper.toResponse(service.update(id, dto)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/filtro-ativo/{filtro}")
    public ResponseEntity<List<CardapioResponseDto>> findByIsAtivo(@PathVariable Boolean filtro) {
        List<CardapioResponseDto> result = CardapioMapper.toResponseDto(service.findByIsAtivo(filtro));
        if (result.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(result);
    }

    @PatchMapping("/{id}/ativo")
    public ResponseEntity<CardapioResponseDto> updateAtivo(@PathVariable Integer id,
                                                           @RequestBody @Valid CardapioAtivoDto dto) {
        return ResponseEntity.ok(CardapioMapper.toResponse(service.updateAtivo(id, dto.getAtivo())));
    }
}