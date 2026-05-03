package com.temnafesta.controller;

import com.temnafesta.dto.pagamento.PagamentoRequestDto;
import com.temnafesta.dto.pagamento.PagamentoResponseDto;
import com.temnafesta.mapper.PagamentoMapper;
import com.temnafesta.model.Pagamento;
import com.temnafesta.service.PagamentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/pagamentos")
@Tag(name = "Pagamentos", description = "Pagamentos associados aos pedidos")
public class PagamentoController {

    private final PagamentoService service;

    public PagamentoController(PagamentoService service) {
        this.service = service;
    }

    @Operation(summary = "Cria um novo pagamento")
    @ApiResponse(responseCode = "201", description = "Pagamento criado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    @PostMapping
    public ResponseEntity<PagamentoResponseDto> criar(@RequestBody @Valid PagamentoRequestDto dto) {
        Pagamento criado = service.criar(dto);
        PagamentoResponseDto response = PagamentoMapper.toResponseDto(criado);
        URI location = URI.create("/pagamentos/" + criado.getId());
        return ResponseEntity.created(location).body(response);
    }

    @Operation(summary = "Lista todos os pagamentos")
    @ApiResponse(responseCode = "200", description = "Listagem realizada com sucesso")
    @ApiResponse(responseCode = "204", description = "Nenhum pagamento encontrado")
    @GetMapping
    public ResponseEntity<List<PagamentoResponseDto>> listar() {
        List<PagamentoResponseDto> pagamentos = PagamentoMapper.toResponseDtoList(service.listar());
        if (pagamentos.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(pagamentos);
    }

    @Operation(summary = "Busca pagamento por ID")
    @ApiResponse(responseCode = "200", description = "Pagamento encontrado com sucesso")
    @ApiResponse(responseCode = "404", description = "Pagamento não encontrado")
    @GetMapping("/{id}")
    public ResponseEntity<PagamentoResponseDto> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(PagamentoMapper.toResponseDto(service.buscarPorId(id)));
    }

    @Operation(summary = "Lista pagamentos por pedido")
    @ApiResponse(responseCode = "200", description = "Listagem realizada com sucesso")
    @ApiResponse(responseCode = "204", description = "Nenhum pagamento encontrado")
    @GetMapping("/pedido/{pedidoId}")
    public ResponseEntity<List<PagamentoResponseDto>> listarPorPedido(@PathVariable Integer pedidoId) {
        List<PagamentoResponseDto> pagamentos = PagamentoMapper.toResponseDtoList(service.listarPorPedido(pedidoId));
        if (pagamentos.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(pagamentos);
    }

    @Operation(summary = "Remove um pagamento")
    @ApiResponse(responseCode = "204", description = "Pagamento removido com sucesso")
    @ApiResponse(responseCode = "404", description = "Pagamento não encontrado")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}