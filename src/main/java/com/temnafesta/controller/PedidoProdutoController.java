package com.temnafesta.controller;

import com.temnafesta.dto.pedidoproduto.PedidoProdutoRequestDto;
import com.temnafesta.dto.pedidoproduto.PedidoProdutoResponseDto;
import com.temnafesta.mapper.ItemPedidoMapper;
import com.temnafesta.model.ItemPedido;
import com.temnafesta.service.PedidoProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/pedidos/{pedidoId}/produtos")
@Tag(name = "Pedido-Produto", description = "Itens (produtos) vinculados a um pedido")
public class PedidoProdutoController {

    private final PedidoProdutoService service;

    public PedidoProdutoController(PedidoProdutoService service) {
        this.service = service;
    }

    @Operation(summary = "Adiciona um produto a um pedido")
    @ApiResponse(responseCode = "201", description = "Produto adicionado ao pedido com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    @PostMapping
    public ResponseEntity<PedidoProdutoResponseDto> criar(
            @PathVariable Integer pedidoId,
            @RequestBody @Valid PedidoProdutoRequestDto dto
    ) {
        ItemPedido itemPedido = ItemPedidoMapper.toEntity(dto);
        ItemPedido criado = service.criar(itemPedido, pedidoId, dto.getProdutoId());
        URI location = URI.create("/pedidos/" + pedidoId + "/produtos/" + criado.getId());
        return ResponseEntity.created(location).body(ItemPedidoMapper.toResponseDto(criado));
    }

    @Operation(summary = "Lista produtos de um pedido")
    @ApiResponse(responseCode = "200", description = "Listagem realizada com sucesso")
    @GetMapping
    public ResponseEntity<List<PedidoProdutoResponseDto>> listarPorPedido(@PathVariable Integer pedidoId) {
        List<ItemPedido> itemPedidos = service.listarPorPedido(pedidoId);
        return ResponseEntity.ok(ItemPedidoMapper.toResponseDtoList(itemPedidos));
    }

    @Operation(summary = "Busca produto de um pedido por ID")
    @ApiResponse(responseCode = "200", description = "Item encontrado com sucesso")
    @ApiResponse(responseCode = "404", description = "Item não encontrado")
    @GetMapping("/{id}")
    public ResponseEntity<PedidoProdutoResponseDto> buscarPorId(
            @PathVariable Integer pedidoId,
            @PathVariable Integer id
    ) {
        ItemPedido itemPedido = service.buscarPorId(pedidoId, id);
        return ResponseEntity.ok(ItemPedidoMapper.toResponseDto(itemPedido));
    }

    @Operation(summary = "Atualiza um produto de um pedido")
    @ApiResponse(responseCode = "200", description = "Item atualizado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    @ApiResponse(responseCode = "404", description = "Item não encontrado")
    @PutMapping("/{id}")
    public ResponseEntity<PedidoProdutoResponseDto> atualizar(
            @PathVariable Integer pedidoId,
            @PathVariable Integer id,
            @RequestBody @Valid PedidoProdutoRequestDto dto
    ) {
        ItemPedido itemPedido = ItemPedidoMapper.toEntity(dto);
        ItemPedido atualizado = service.atualizar(id, itemPedido, pedidoId, dto.getProdutoId());
        return ResponseEntity.ok(ItemPedidoMapper.toResponseDto(atualizado));
    }

    @Operation(summary = "Remove um produto de um pedido")
    @ApiResponse(responseCode = "204", description = "Item removido com sucesso")
    @ApiResponse(responseCode = "404", description = "Item não encontrado")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(
            @PathVariable Integer pedidoId,
            @PathVariable Integer id
    ) {
        service.deletar(pedidoId, id);
        return ResponseEntity.noContent().build();
    }
}