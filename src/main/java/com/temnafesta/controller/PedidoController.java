package com.temnafesta.controller;

import com.temnafesta.dto.pedido.PedidoRequestDto;
import com.temnafesta.dto.pedido.PedidoResponseDto;
import com.temnafesta.mapper.PedidoMapper;
import com.temnafesta.model.Pedido;
import com.temnafesta.model.StatusProducao;
import com.temnafesta.service.PedidoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    private final PedidoService service;

    public PedidoController(PedidoService service) {
        this.service = service;
    }

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

    @GetMapping
    public ResponseEntity<List<PedidoResponseDto>> listarPedidos(
            @RequestParam(defaultValue = "andamento") String filtro
    ) {
        List<PedidoResponseDto> pedidos;
        if (filtro.equalsIgnoreCase("todos")) {
            pedidos = service.listarTodos();

        } else if (filtro.equalsIgnoreCase("validos")) {
            pedidos = service.listarPedidosValidos();

        } else {
            pedidos = service.listarPedidosEmAndamento();
        }

        if (pedidos.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(pedidos);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<PedidoResponseDto>> listarPorStatus(
            @PathVariable StatusProducao status
    ) {
        List<PedidoResponseDto> pedidos = service.listarPorStatus(status);
        if (pedidos.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(pedidos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponseDto> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

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

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<Void> cancelarPedido(
            @PathVariable Integer id,
            @RequestParam Integer usuarioId
    ) {
        service.cancelar(id,usuarioId);
        return ResponseEntity.noContent().build();
    }
}