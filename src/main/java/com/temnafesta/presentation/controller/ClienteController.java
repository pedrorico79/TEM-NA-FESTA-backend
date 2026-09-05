package com.temnafesta.presentation.controller;

import com.temnafesta.application.dto.AlternarAtivoClienteCommand;
import com.temnafesta.application.dto.AtualizarClienteCommand;
import com.temnafesta.application.dto.CriarClienteCommand;
import com.temnafesta.application.usecase.*;
import com.temnafesta.domain.model.Cliente;
import com.temnafesta.presentation.dto.AlternarStatusRequestDto;
import com.temnafesta.presentation.dto.AtualizarClienteRequestDto;
import com.temnafesta.presentation.dto.ClienteResponseDto;
import com.temnafesta.presentation.dto.CriarClienteRequestDto;
import com.temnafesta.presentation.mapper.ClientePresentationMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/clientes")
@Tag(name = "Clientes", description = "Gerenciamento de clientes da confeitaria")
public class ClienteController {

    private final CriarClienteUseCase criarClienteUseCase;
    private final ClientePresentationMapper mapper;
    private final ListarClientesUseCase listarClientesUseCase;
    private final BuscarClientePorIdUseCase buscarClientePorIdUseCase;
    private final AtualizarClienteUseCase atualizarClienteUseCase;
    private final AlternarAtivoClienteUseCase alternarAtivoClienteUseCase;

    public ClienteController(
            CriarClienteUseCase criarClienteUseCase,
            ClientePresentationMapper mapper,
            ListarClientesUseCase listarClientesUseCase,
            AtualizarClienteUseCase atualizarClienteUseCase,
            BuscarClientePorIdUseCase buscarClientePorIdUseCase,
            AlternarAtivoClienteUseCase alternarAtivoClienteUseCase) {
        this.criarClienteUseCase = criarClienteUseCase;
        this.mapper = mapper;
        this.listarClientesUseCase = listarClientesUseCase;
        this.atualizarClienteUseCase = atualizarClienteUseCase;
        this.alternarAtivoClienteUseCase = alternarAtivoClienteUseCase;
        this.buscarClientePorIdUseCase = buscarClientePorIdUseCase;
    }

    @PostMapping
    @Operation(summary = "Cadastra um novo cliente com endereço opcional")
    public ResponseEntity<ClienteResponseDto> criar(@Valid @RequestBody CriarClienteRequestDto request) {
        CriarClienteCommand command = mapper.toCommand(request);
        Cliente clienteSalvo = criarClienteUseCase.executar(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(clienteSalvo));
    }


    @GetMapping
    @Operation(summary = "Lista os clientes não deletados, com busca opcional e ativos primeiro",
            description = "A busca considera nome, telefone, WhatsApp e Instagram.")
    public ResponseEntity<List<ClienteResponseDto>> listar(
            @RequestParam(required = false) String busca
    ) {
        //TODO: alterar DTO de retorno para objeto mais simples (sem info de endereço/denecessárias)
        // para tornar as requisições mais eficientes
        List<ClienteResponseDto> response = listarClientesUseCase.executar(busca)
                .stream()
                .map(mapper::toResponse)
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca um cliente pelo ID")
    public ResponseEntity<ClienteResponseDto> buscarPorId(@PathVariable Long id) {
        Cliente cliente = buscarClientePorIdUseCase.executar(id);
        return ResponseEntity.ok(mapper.toResponse(cliente));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza os dados de um cliente existente")
    public ResponseEntity<ClienteResponseDto> atualizar(@PathVariable Long id, @Valid @RequestBody AtualizarClienteRequestDto request) {
        AtualizarClienteCommand command = mapper.toCommand(request, id);
        Cliente clienteAtualizado = atualizarClienteUseCase.executar(command);
        return ResponseEntity.ok(mapper.toResponse(clienteAtualizado));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Altera o status ativo/inativo de um cliente",
            description = "Impede a desativação caso existam pedidos em andamento.")
    public ResponseEntity<ClienteResponseDto> alternarStatus(
            @PathVariable Long id,
            @Valid @RequestBody AlternarStatusRequestDto request
    ) {
        AlternarAtivoClienteCommand command = new AlternarAtivoClienteCommand(id, request.ativo());

        Cliente clienteAtualizado = alternarAtivoClienteUseCase.executar(command);

        return ResponseEntity.ok(mapper.toResponse(clienteAtualizado));
    }
}