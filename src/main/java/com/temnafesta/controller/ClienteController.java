package com.temnafesta.controller;

import com.temnafesta.dto.cliente.ClienteRequestDto;
import com.temnafesta.dto.cliente.ClienteResponseDto;
import com.temnafesta.mapper.ClienteMapper;
import com.temnafesta.model.Cliente;
import com.temnafesta.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes")
@Tag(name = "Clientes", description = "Cadastro e gerenciamento de clientes")
public class ClienteController {

    private final ClienteService service;

    public ClienteController(ClienteService service) {
        this.service = service;
    }

    @Operation(summary = "Cria um novo cliente")
    @ApiResponse(responseCode = "201", description = "Cliente criado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    @PostMapping
    public ResponseEntity<ClienteResponseDto> criar(@RequestBody @Valid ClienteRequestDto dto){
        Cliente cliente = ClienteMapper.toEntity(dto);
        Cliente criado = service.criar(cliente, dto.getEnderecoId());
        return ResponseEntity.status(201).body(ClienteMapper.toResponseDto(criado));
    }

    @Operation(summary = "Lista todos os clientes")
    @ApiResponse(responseCode = "200", description = "Listagem realizada com sucesso")
    @GetMapping
    public ResponseEntity<List<ClienteResponseDto>> listar(
            @RequestParam(required = false, defaultValue = "true") Boolean apenasAtivos
    ){
        List<Cliente> clientes;
        if (apenasAtivos) {
            clientes = service.listarAtivos();
        } else {
            clientes = service.listarTodos();
        }

        List<ClienteResponseDto> clienteResponseDto = ClienteMapper.toResponseDtoList(clientes);
        return ResponseEntity.ok(clienteResponseDto);
    }

    @Operation(summary = "Busca cliente por ID")
    @ApiResponse(responseCode = "200", description = "Cliente encontrado com sucesso")
    @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDto> buscarPorId(@PathVariable Integer id){
        Cliente cliente = service.buscarPorId(id);
        return ResponseEntity.ok(ClienteMapper.toResponseDto(cliente));
    }

    @Operation(summary = "Atualiza um cliente existente")
    @ApiResponse(responseCode = "200", description = "Cliente atualizado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponseDto> atualizar(@PathVariable Integer id, @RequestBody @Valid ClienteRequestDto dto){
        Cliente cliente = ClienteMapper.toEntity(dto);
        Cliente atualizado = service.atualizar(id, cliente, dto.getEnderecoId());
        return ResponseEntity.ok(ClienteMapper.toResponseDto(atualizado));
    }

    @Operation(summary = "Desativa um cliente")
    @ApiResponse(responseCode = "204", description = "Cliente desativado com sucesso")
    @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    @PatchMapping("/{id}/desativar")
    public ResponseEntity<Void> desativar(@PathVariable Integer id){
        service.desativar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Reativa um cliente")
    @ApiResponse(responseCode = "204", description = "Cliente reativado com sucesso")
    @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    @PatchMapping("/{id}/reativar")
    public ResponseEntity<Void> reativar(@PathVariable Integer id){
        service.reativar(id);
        return ResponseEntity.noContent().build();
    }
}