package com.temnafesta.controller;


import com.temnafesta.dto.cliente.ClienteRequestDto;
import com.temnafesta.dto.cliente.ClienteResponseDto;
import com.temnafesta.mapper.ClienteMapper;
import com.temnafesta.model.Cliente;
import com.temnafesta.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteService service;

    public ClienteController(ClienteService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ClienteResponseDto> criar(@RequestBody @Valid ClienteRequestDto dto){
        Cliente cliente = ClienteMapper.toEntity(dto);
        Cliente criado = service.criar(cliente, dto.getEnderecoId());
        return ResponseEntity.status(201).body(ClienteMapper.toResponseDto(criado));
    }

    @GetMapping
    public ResponseEntity<List<ClienteResponseDto>> listar(){
        List<Cliente> clientes = service.listar();
        return ResponseEntity.ok(ClienteMapper.toResponseDtoList(clientes));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDto> buscarPorId(@PathVariable Integer id){
        Cliente cliente = service.buscarPorId(id);
        return ResponseEntity.ok(ClienteMapper.toResponseDto(cliente));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponseDto> atualizar(@PathVariable Integer id, @RequestBody @Valid ClienteRequestDto dto){
        Cliente cliente = ClienteMapper.toEntity(dto);
        Cliente atualizado = service.atualizar(id, cliente, dto.getEnderecoId());
        return ResponseEntity.ok(ClienteMapper.toResponseDto(atualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id){
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
