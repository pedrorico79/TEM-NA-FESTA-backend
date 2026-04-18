package com.temnafesta.controller;

import com.temnafesta.dto.pagamento.PagamentoRequestDto;
import com.temnafesta.dto.pagamento.PagamentoResponseDto;
import com.temnafesta.mapper.PagamentoMapper;
import com.temnafesta.model.Pagamento;
import com.temnafesta.service.PagamentoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/pagamentos")
public class PagamentoController {

    private final PagamentoService service;

    public PagamentoController(PagamentoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<PagamentoResponseDto> criar(@RequestBody @Valid PagamentoRequestDto dto) {
        Pagamento criado = service.criar(dto);
        PagamentoResponseDto response = PagamentoMapper.toResponseDto(criado);
        URI location = URI.create("/pagamentos/" + criado.getId());
        return ResponseEntity.created(location).body(response);
    }

    @GetMapping
    public ResponseEntity<List<PagamentoResponseDto>> listar() {
        List<PagamentoResponseDto> pagamentos = PagamentoMapper.toResponseDtoList(service.listar());
        if (pagamentos.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(pagamentos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PagamentoResponseDto> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(PagamentoMapper.toResponseDto(service.buscarPorId(id)));
    }

    @GetMapping("/pedido/{pedidoId}")
    public ResponseEntity<List<PagamentoResponseDto>> listarPorPedido(@PathVariable Integer pedidoId) {
        List<PagamentoResponseDto> pagamentos = PagamentoMapper.toResponseDtoList(service.listarPorPedido(pedidoId));
        if (pagamentos.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(pagamentos);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}