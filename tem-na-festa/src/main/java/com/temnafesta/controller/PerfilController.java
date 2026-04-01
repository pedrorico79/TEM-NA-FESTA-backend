package com.temnafesta.controller;

import com.temnafesta.dto.perfil.PerfilRequestDto;
import com.temnafesta.dto.perfil.PerfilResponseDto;
import com.temnafesta.mapper.PerfilMapper;
import com.temnafesta.model.Perfil;
import com.temnafesta.service.PerfilService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/perfil")
public class PerfilController {

    private final PerfilService service;

    public PerfilController(PerfilService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<PerfilResponseDto> criar(@RequestBody @Valid PerfilRequestDto dto) {
        Perfil perfil = PerfilMapper.toEntity(dto);
        Perfil criado = service.criar(perfil);
        return ResponseEntity.status(201).body(PerfilMapper.toResponseDto(criado));
    }

    @GetMapping
    public ResponseEntity<List<PerfilResponseDto>> listar() {
        List<Perfil> perfis = service.listar();
        return ResponseEntity.ok(PerfilMapper.toResponseDtoList(perfis));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PerfilResponseDto> buscarPorId(@PathVariable Integer id) {
        Perfil perfil = service.buscarPorId(id);
        return ResponseEntity.ok(PerfilMapper.toResponseDto(perfil));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PerfilResponseDto> atualizar(@PathVariable Integer id, @RequestBody @Valid PerfilRequestDto dto) {
        Perfil perfil = PerfilMapper.toEntity(dto);
        Perfil atualizado = service.atualizar(id, perfil);
        return ResponseEntity.ok(PerfilMapper.toResponseDto(atualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
