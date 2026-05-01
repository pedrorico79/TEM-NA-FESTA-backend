package com.temnafesta.controller;

import com.temnafesta.dto.pedidoproduto.PedidoProdutoRequestDto;
import com.temnafesta.dto.pedidoproduto.PedidoProdutoResponseDto;
import com.temnafesta.mapper.PedidoProdutoMapper;
import com.temnafesta.model.PedidoProduto;
import com.temnafesta.service.PedidoProdutoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/pedidos/{pedidoId}/produtos")
public class PedidoProdutoController {

    private final PedidoProdutoService service;

    public PedidoProdutoController(PedidoProdutoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<PedidoProdutoResponseDto> criar(
            @PathVariable Integer pedidoId,
            @RequestBody @Valid PedidoProdutoRequestDto dto
    ) {
        PedidoProduto pedidoProduto = PedidoProdutoMapper.toEntity(dto);
        PedidoProduto criado = service.criar(pedidoProduto, pedidoId, dto.getProdutoId());
        URI location = URI.create("/pedidos/" + pedidoId + "/produtos/" + criado.getId());
        return ResponseEntity.created(location).body(PedidoProdutoMapper.toResponseDto(criado));
    }

    @GetMapping
    public ResponseEntity<List<PedidoProdutoResponseDto>> listarPorPedido(@PathVariable Integer pedidoId) {
        List<PedidoProduto> pedidoProdutos = service.listarPorPedido(pedidoId);
        return ResponseEntity.ok(PedidoProdutoMapper.toResponseDtoList(pedidoProdutos));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoProdutoResponseDto> buscarPorId(
            @PathVariable Integer pedidoId,
            @PathVariable Integer id
    ) {
        PedidoProduto pedidoProduto = service.buscarPorId(pedidoId, id);
        return ResponseEntity.ok(PedidoProdutoMapper.toResponseDto(pedidoProduto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PedidoProdutoResponseDto> atualizar(
            @PathVariable Integer pedidoId,
            @PathVariable Integer id,
            @RequestBody @Valid PedidoProdutoRequestDto dto
    ) {
        PedidoProduto pedidoProduto = PedidoProdutoMapper.toEntity(dto);
        PedidoProduto atualizado = service.atualizar(id, pedidoProduto, pedidoId, dto.getProdutoId());
        return ResponseEntity.ok(PedidoProdutoMapper.toResponseDto(atualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(
            @PathVariable Integer pedidoId,
            @PathVariable Integer id
    ) {
        service.deletar(pedidoId, id);
        return ResponseEntity.noContent().build();
    }
}