package com.temnafesta.controller;

import com.temnafesta.dto.usuario.UsuarioRequestDto;
import com.temnafesta.dto.usuario.UsuarioResponseDto;
import com.temnafesta.mapper.UsuarioMapper;
import com.temnafesta.model.Usuario;
import com.temnafesta.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<UsuarioResponseDto> criar(@RequestBody @Valid UsuarioRequestDto dto) {
        Usuario usuario = UsuarioMapper.toEntity(dto);
        Usuario criado = service.criar(usuario, dto.getPerfilId());
        return ResponseEntity.status(201).body(UsuarioMapper.toResponseDto(criado));
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponseDto>> listar() {
        List<Usuario> usuarios = service.listar();
        return ResponseEntity.ok(UsuarioMapper.toResponseDtoList(usuarios));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDto> buscarPorId(@PathVariable Integer id) {
        Usuario usuario = service.buscarPorId(id);
        return ResponseEntity.ok(UsuarioMapper.toResponseDto(usuario));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDto> atualizar(@PathVariable Integer id, @RequestBody @Valid UsuarioRequestDto dto) {
        Usuario usuario = UsuarioMapper.toEntity(dto);
        Usuario atualizado = service.atualizar(id, usuario, dto.getPerfilId());
        return ResponseEntity.ok(UsuarioMapper.toResponseDto(atualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
