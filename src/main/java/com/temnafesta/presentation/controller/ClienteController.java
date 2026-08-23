package com.temnafesta.presentation.controller;

import com.temnafesta.application.dto.CriarClienteCommand;
import com.temnafesta.application.dto.ListarClientesQuery;
import com.temnafesta.application.usecase.CriarClienteUseCase;
import com.temnafesta.application.usecase.ListarClientesUseCase;
import com.temnafesta.domain.model.Cliente;
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

    public ClienteController(
            CriarClienteUseCase criarClienteUseCase,
            ClientePresentationMapper mapper,
            ListarClientesUseCase listarClientesUseCase) {
        this.criarClienteUseCase = criarClienteUseCase;
        this.mapper = mapper;
        this.listarClientesUseCase = listarClientesUseCase;
    }

    @PostMapping
    @Operation(summary = "Cadastra um novo cliente com endereço opcional")
    public ResponseEntity<ClienteResponseDto> criar(@Valid @RequestBody CriarClienteRequestDto request) {
        CriarClienteCommand command = mapper.toCommand(request);
        Cliente clienteSalvo = criarClienteUseCase.executar(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(clienteSalvo));
    }


    @GetMapping
    @Operation(summary = "Lista clientes com paginação e filtro opcional por nome")
    public ResponseEntity<List<ClienteResponseDto>> listar(
            @RequestParam(required = false) String busca,
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "10") int tamanho
    ) {
        ListarClientesQuery query = new ListarClientesQuery(busca, pagina, tamanho);

        List<ClienteResponseDto> response = listarClientesUseCase.executar(query)
                .stream()
                .map(mapper::toResponse)
                .toList();

        return ResponseEntity.ok(response);
    }
}