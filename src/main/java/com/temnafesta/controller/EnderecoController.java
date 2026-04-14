package com.temnafesta.controller;


import com.temnafesta.dto.endereco.EnderecoRequestDto;
import com.temnafesta.dto.endereco.EnderecoResponseDto;
import com.temnafesta.mapper.EnderecoMapper;
import com.temnafesta.model.Endereco;
import com.temnafesta.service.EnderecoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/enderecos")
public class EnderecoController {

    private final EnderecoService enderecoService;

    public EnderecoController(EnderecoService enderecoService) {
        this.enderecoService = enderecoService;
    }

    @GetMapping
    public ResponseEntity<List<EnderecoResponseDto>> listar(){
        List<Endereco> enderecos = enderecoService.listar();
        List<EnderecoResponseDto> responseList = EnderecoMapper.toResponseList(enderecos);
        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EnderecoResponseDto> buscarPorId(@PathVariable Integer id){
        Endereco endereco = enderecoService.buscarPorId(id);
        EnderecoResponseDto response = EnderecoMapper.toResponse(endereco);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<EnderecoResponseDto> criar(@RequestBody @Valid EnderecoRequestDto requestDto){
        Endereco entity = enderecoService.criar(requestDto);
        EnderecoResponseDto response = EnderecoMapper.toResponse(entity);
        return ResponseEntity.status(201).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EnderecoResponseDto> atualizarPorId(@PathVariable Integer id, @RequestBody @Valid EnderecoRequestDto requestDto){
        Endereco entity = enderecoService.atualizar(id, requestDto);
        EnderecoResponseDto response = EnderecoMapper.toResponse(entity);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarPorId(@PathVariable Integer id){
        enderecoService.deletar(id);
        return ResponseEntity.status(204).build();
    }
}
