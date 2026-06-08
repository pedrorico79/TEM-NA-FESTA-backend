package com.temnafesta.controller;

import com.temnafesta.dto.countPedidos.CountPedidosResponseDto;
import com.temnafesta.dto.historicosStatusPedido.HistoricoStatusPedidoResponseDto;
import com.temnafesta.dto.pedido.PedidoRequestDto;
import com.temnafesta.dto.pedido.PedidoResponseDto;
import com.temnafesta.mapper.PedidoMapper;
import com.temnafesta.model.Pedido;
import com.temnafesta.model.StatusProducao;
import com.temnafesta.service.HistoricoStatusPedidoService;
import com.temnafesta.service.PedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import jakarta.validation.constraints.Positive;
import org.springframework.validation.annotation.Validated;


import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/pedidos")
@Validated
@Tag(name = "Pedidos", description = "Gestão de pedidos de encomenda")
public class PedidoController {

    private final PedidoService service;
    private final HistoricoStatusPedidoService historicoService;

    public PedidoController(PedidoService service, HistoricoStatusPedidoService historicoService) {
        this.service = service;
        this.historicoService = historicoService;
    }

    @Operation(summary = "Cria um novo pedido")
    @ApiResponse(responseCode = "201", description = "Pedido criado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    @PostMapping
    public ResponseEntity<PedidoResponseDto> criar(@RequestBody @Valid PedidoRequestDto dto) {
        Pedido pedido = PedidoMapper.toEntity(dto);

        Pedido criado = service.criarComProdutos(
                pedido,
                dto.getClienteId(),
                dto.getUsuarioId(),
                dto.getStatusProducaoId(),
                dto.getCampanhaId(),
                dto.getProdutos()
        );

        PedidoResponseDto response = service.buscarPorId(criado.getId());
        URI location = URI.create("/pedidos/" + criado.getId());
        return ResponseEntity.created(location).body(response);
    }


    // ============================ ADAPTAR CONFORME PLANILHA
    @Operation(summary = "Lista todos os pedidos")
    @ApiResponse(responseCode = "200", description = "Listagem realizada com sucesso")
    @ApiResponse(responseCode = "204", description = "Nenhum pedido encontrado")
    @GetMapping
    public ResponseEntity<List<PedidoResponseDto>> listar(
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


    // ======= ADAPTAR PARA COUNT BY STATUS CONFORME PLANILHA
    @Operation(summary = "Lista pedidos por status de produção")
    @ApiResponse(responseCode = "200", description = "Listagem realizada com sucesso")
    @ApiResponse(responseCode = "204", description = "Nenhum pedido encontrado")
    @GetMapping("/status/{statusId}")
    public ResponseEntity<List<PedidoResponseDto>> listarPorStatus(
            @PathVariable Integer statusId
    ) {
        List<PedidoResponseDto> pedidos = service.listarPorStatus(statusId);
        if (pedidos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
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
    public ResponseEntity<PedidoResponseDto> atualizar(
            @PathVariable Integer id,
            @RequestBody @Valid PedidoRequestDto dto) {

        Pedido pedido = PedidoMapper.toEntity(dto);

        Pedido atualizado = service.atualizar(
                id,
                pedido,
                dto.getClienteId(),
                dto.getUsuarioId(),
                dto.getStatusProducaoId(), // agora o DTO deve ter o campo statusProducaoId
                dto.getCampanhaId()
        );

        return ResponseEntity.ok(service.buscarPorId(atualizado.getId()));
    }


    @Operation(summary = "Cancela um pedido")
    @ApiResponse(responseCode = "204", description = "Pedido cancelado com sucesso")
    @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<Void> cancelar(
            @PathVariable Integer id,
            @RequestParam Integer usuarioId
    ) {
        service.cancelar(id, usuarioId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Lista os próximos pedidos para retirada")
    @ApiResponse(responseCode = "200", description = "Pedidos encontrados")
    @ApiResponse(responseCode = "204", description = "Nenhum pedido encontrado")
    @GetMapping("/proximas-retiradas")
    public ResponseEntity<Page<PedidoResponseDto>> listarProximasRetiradas(
            @RequestParam
            @Positive(message = "dias deve ser maior que zero")
            Integer dias,

            @RequestParam(defaultValue = "0")
            Integer page
    ) {
        Page<PedidoResponseDto> pedidos =
                service.listarProximasRetiradas(dias, page);

        if (pedidos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(pedidos);
    }

    @Operation(summary = "Lista o histórico de status do pedido")
    @ApiResponse(responseCode = "200", description = "Histórico encontrado")
    @ApiResponse(responseCode = "204", description = "Pedido sem histórico")
    @GetMapping("/{id}/historico-status")
    public ResponseEntity<List<HistoricoStatusPedidoResponseDto>>
    listarHistoricoStatus(
            @PathVariable Integer id
    ) {

        List<HistoricoStatusPedidoResponseDto> historico =
                historicoService.listarPorPedido(id);

        if (historico.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(historico);
    }

    @GetMapping("/count-pedidos")
    public ResponseEntity<CountPedidosResponseDto> countPedidos(
            @RequestParam Integer dias
    ) {
        return ResponseEntity.ok(
                service.contarPedidos(dias)
        );
    }
}