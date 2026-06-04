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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public ResponseEntity<ClienteResponseDto> criar(@RequestBody @Valid ClienteRequestDto dto) {
        Cliente cliente = ClienteMapper.toEntity(dto);
        Cliente criado = service.criar(cliente, dto.getEnderecoId());
        return ResponseEntity.status(201).body(ClienteMapper.toResponse(criado));
    }

    @Operation(summary = "Lista clientes com busca paginada")
    @ApiResponse(responseCode = "200", description = "Listagem realizada com sucesso")
    @GetMapping
    public ResponseEntity<Page<ClienteResponseDto>> listar(
            @RequestParam(required = false) String busca,
            Pageable pageable
    ) {
        return ResponseEntity.ok(
                service.listar(busca, pageable)
                        .map(ClienteMapper::toResponse)
        );
    }

    @Operation(summary = "Busca cliente por ID")
    @ApiResponse(responseCode = "200", description = "Cliente encontrado com sucesso")
    @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDto> buscarPorId(@PathVariable Integer id) {
        Cliente cliente = service.buscarPorId(id);
        return ResponseEntity.ok(ClienteMapper.toResponse(cliente));
    }

    @Operation(summary = "Atualiza um cliente existente")
    @ApiResponse(responseCode = "200", description = "Cliente atualizado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponseDto> atualizar(
            @PathVariable Integer id,
            @RequestBody @Valid ClienteRequestDto dto) {

        Cliente cliente = ClienteMapper.toEntity(dto);
        Cliente atualizado = service.atualizar(id, cliente, dto.getEnderecoId());
        return ResponseEntity.ok(ClienteMapper.toResponse(atualizado));
    }

    @Operation(summary = "Ativa ou desativa um cliente")
    @ApiResponse(responseCode = "204", description = "Status atualizado com sucesso")
    @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    @PatchMapping("/{id}/ativo")
    public ResponseEntity<Void> toggleAtivo(@PathVariable Integer id) {
        service.toggleAtivo(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Remove um cliente")
    @ApiResponse(responseCode = "204", description = "Cliente removido com sucesso")
    @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}