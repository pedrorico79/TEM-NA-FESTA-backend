package com.temnafesta.controller;

import com.temnafesta.dto.pedidoproduto.PedidoProdutoRequestDto;
import com.temnafesta.dto.pedidoproduto.PedidoProdutoResponseDto;
import com.temnafesta.mapper.PedidoProdutoMapper;
import com.temnafesta.model.PedidoProduto;
import com.temnafesta.service.PedidoProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pedido-produto")
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
    public ResponseEntity<PedidoProdutoResponseDto> criar(@RequestBody @Valid PedidoProdutoRequestDto dto) {
        PedidoProduto pedidoProduto = PedidoProdutoMapper.toEntity(dto);
        PedidoProduto criado = service.criar(pedidoProduto, dto.getPedidoId(), dto.getProdutoId());
        return ResponseEntity.status(201).body(PedidoProdutoMapper.toResponseDto(criado));
    }

    @Operation(summary = "Lista todos os itens de pedidos")
    @ApiResponse(responseCode = "200", description = "Listagem realizada com sucesso")
    @GetMapping
    public ResponseEntity<List<PedidoProdutoResponseDto>> listar() {
        List<PedidoProduto> pedidoProdutos = service.listar();
        return ResponseEntity.ok(PedidoProdutoMapper.toResponseDtoList(pedidoProdutos));
    }

    @Operation(summary = "Lista itens por pedido")
    @ApiResponse(responseCode = "200", description = "Listagem realizada com sucesso")
    @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    @GetMapping("/pedido/{pedidoId}")
    public ResponseEntity<List<PedidoProdutoResponseDto>> listarPorPedido(@PathVariable Integer pedidoId) {
        List<PedidoProduto> pedidoProdutos = service.listarPorPedido(pedidoId);
        return ResponseEntity.ok(PedidoProdutoMapper.toResponseDtoList(pedidoProdutos));
    }

    @Operation(summary = "Busca item de pedido por ID")
    @ApiResponse(responseCode = "200", description = "Item encontrado com sucesso")
    @ApiResponse(responseCode = "404", description = "Item não encontrado")
    @GetMapping("/{id}")
    public ResponseEntity<PedidoProdutoResponseDto> buscarPorId(@PathVariable Integer id) {
        PedidoProduto pedidoProduto = service.buscarPorId(id);
        return ResponseEntity.ok(PedidoProdutoMapper.toResponseDto(pedidoProduto));
    }

    @Operation(summary = "Atualiza um item de pedido")
    @ApiResponse(responseCode = "200", description = "Item atualizado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    @ApiResponse(responseCode = "404", description = "Item não encontrado")
    @PutMapping("/{id}")
    public ResponseEntity<PedidoProdutoResponseDto> atualizar(@PathVariable Integer id, @RequestBody @Valid PedidoProdutoRequestDto dto) {
        PedidoProduto pedidoProduto = PedidoProdutoMapper.toEntity(dto);
        PedidoProduto atualizado = service.atualizar(id, pedidoProduto, dto.getPedidoId(), dto.getProdutoId());
        return ResponseEntity.ok(PedidoProdutoMapper.toResponseDto(atualizado));
    }

    @Operation(summary = "Remove um item de pedido")
    @ApiResponse(responseCode = "204", description = "Item removido com sucesso")
    @ApiResponse(responseCode = "404", description = "Item não encontrado")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}