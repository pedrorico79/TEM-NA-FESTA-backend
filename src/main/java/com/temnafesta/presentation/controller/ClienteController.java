package com.temnafesta.presentation.controller;

import com.temnafesta.application.dto.AlterarAtivoClienteCommand;
import com.temnafesta.application.dto.AtualizarClienteCommand;
import com.temnafesta.application.dto.CriarClienteCommand;
import com.temnafesta.application.usecase.*;
import com.temnafesta.domain.model.Cliente;
import com.temnafesta.presentation.dto.AlterarAtivoClienteRequestDto;
import com.temnafesta.presentation.dto.AtualizarClienteRequestDto;
import com.temnafesta.presentation.dto.ClienteResponseDto;
import com.temnafesta.presentation.dto.CriarClienteRequestDto;
import com.temnafesta.presentation.mapper.ClientePresentationMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/clientes")
@AllArgsConstructor
@Tag(name = "Clientes", description = "Gerenciamento de clientes da confeitaria")
public class ClienteController {

    private final CriarClienteUseCase criarClienteUseCase;
    private final ClientePresentationMapper mapper;
    private final ListarClientesUseCase listarClientesUseCase;
    private final BuscarClientePorIdUseCase buscarClientePorIdUseCase;
    private final AtualizarClienteUseCase atualizarClienteUseCase;
    private final AlterarAtivoClienteUseCase alterarAtivoClienteUseCase;
    private final DeletarClienteUseCase deletarClienteUseCase;

    @PostMapping
    @Operation(summary = "Cadastra um novo cliente com endereço opcional")
    public ResponseEntity<ClienteResponseDto> criar(@Valid @RequestBody CriarClienteRequestDto request) {
        CriarClienteCommand command = mapper.toCommand(request);
        Cliente clienteSalvo = criarClienteUseCase.executar(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(clienteSalvo));
    }


    @GetMapping
    @Operation(summary = "Lista clientes não deletados, com busca opcional e filtro de ativos",
            description = "Por padrão, inclui ativos e inativos. Use ativos=true para retornar apenas ativos. A busca considera nome, telefone, WhatsApp e Instagram.")
    public ResponseEntity<Page<ClienteResponseDto>> listar(
            @RequestParam(required = false) String busca,
            @RequestParam(defaultValue = "false") boolean ativos,
            Pageable pageable
    ) {
        //TODO: alterar DTO de retorno para objeto mais simples (sem info de endereço/denecessárias)
        // para tornar as requisições mais eficientes
        Page<ClienteResponseDto> response = listarClientesUseCase.executar(busca, ativos, pageable)
                .map(mapper::toResponse);

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

    @PatchMapping("/{id}/ativo")
    @Operation(summary = "Altera o status ativo de um cliente",
            description = "Impede a desativação caso existam pedidos em andamento.")
    @ApiResponse(responseCode = "200", description = "Status atualizado com sucesso")
    @ApiResponse(responseCode = "400", description = "Status não informado ou inválido")
    @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    @ApiResponse(responseCode = "422", description = "Cliente possui pedidos em andamento")
    public ResponseEntity<ClienteResponseDto> alterarAtivo(
            @PathVariable Long id,
            @Valid @RequestBody AlterarAtivoClienteRequestDto request
    ) {
        AlterarAtivoClienteCommand command = mapper.toCommand(request, id);
        Cliente clienteAtualizado = alterarAtivoClienteUseCase.executar(command);

        return ResponseEntity.ok(mapper.toResponse(clienteAtualizado));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Exclui logicamente um cliente",
            description = "Impede a exclusão caso existam pedidos em andamento.")
    @ApiResponse(responseCode = "204", description = "Cliente excluído com sucesso")
    @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    @ApiResponse(responseCode = "422", description = "Cliente possui pedidos em andamento")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        deletarClienteUseCase.executar(id);
        return ResponseEntity.noContent().build();
    }

}
