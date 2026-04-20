package com.temnafesta.controller;

import com.temnafesta.dto.pedido.PedidoRequestDto;
import com.temnafesta.dto.pedido.PedidoResponseDto;
import com.temnafesta.mapper.PedidoMapper;
import com.temnafesta.model.Pedido;
import com.temnafesta.service.PedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/pedidos")
@Tag(name = "Pedidos", description = "Gestão de pedidos de encomenda")
public class PedidoController {

    private final PedidoService service;

    public PedidoController(PedidoService service) {
        this.service = service;
    }

    @Operation(summary = "Cria um novo pedido")
    @ApiResponse(responseCode = "201", description = "Pedido criado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    @PostMapping
    public ResponseEntity<PedidoResponseDto> criarPedido(@RequestBody @Valid PedidoRequestDto dto) {
        Pedido pedido = PedidoMapper.toEntity(dto);

        Pedido criado = service.criar(
                pedido,
                dto.getClienteId(),
                dto.getUsuarioId(),
                dto.getStatusProducao(),
                dto.getCampanhaId()
        );

        PedidoResponseDto response = service.buscarPorId(criado.getId());
        URI location = URI.create("/pedidos/" + criado.getId());
        return ResponseEntity.created(location).body(response);
    }

    @Operation(summary = "Lista todos os pedidos")
    @ApiResponse(responseCode = "200", description = "Listagem realizada com sucesso")
    @ApiResponse(responseCode = "204", description = "Nenhum pedido encontrado")
    @GetMapping
    public ResponseEntity<List<PedidoResponseDto>> listarPedidos() {
        List<PedidoResponseDto> pedidos = service.listar();
        if (pedidos.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(pedidos);
    }

    @Operation(summary = "Busca pedido por ID")
    @ApiResponse(responseCode = "200", description = "Pedido encontrado com sucesso")
    @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponseDto> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @Operation(summary = "Atualiza um pedido existente")
    @ApiResponse(responseCode = "200", description = "Pedido atualizado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    @PutMapping("/{id}")
    public ResponseEntity<PedidoResponseDto> atualizarPedido(
            @PathVariable Integer id,
            @RequestBody @Valid PedidoRequestDto dto) {

        Pedido pedido = PedidoMapper.toEntity(dto);

        Pedido atualizado = service.atualizar(
                id,
                pedido,
                dto.getClienteId(),
                dto.getUsuarioId(),
                dto.getStatusProducao(),
                dto.getCampanhaId()
        );

        return ResponseEntity.ok(service.buscarPorId(atualizado.getId()));
    }

    @Operation(summary = "Remove um pedido")
    @ApiResponse(responseCode = "204", description = "Pedido removido com sucesso")
    @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarPedido(@PathVariable Integer id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}