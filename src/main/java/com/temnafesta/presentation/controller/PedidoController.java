package com.temnafesta.presentation.controller;

import com.temnafesta.application.dto.AtualizarPedidoCommand;
import com.temnafesta.application.dto.CriarPedidoCommand;
import com.temnafesta.application.usecase.*;
import com.temnafesta.domain.model.ItemPedido;
import com.temnafesta.domain.model.Pagamento;
import com.temnafesta.domain.model.Pedido;
import com.temnafesta.domain.vo.StatusProducaoEnum;
import com.temnafesta.infrastructure.security.user.UsuarioAutenticado;
import com.temnafesta.presentation.dto.*;
import com.temnafesta.presentation.mapper.PedidoPresentationMapper;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pedidos")
public class PedidoController {

    private final CriarPedidoInternoUseCase criarPedidoInternoUseCase;
    private final AlterarStatusPedidoUseCase alterarStatusPedidoUseCase;
    private final GerarReciboDigitalUseCase gerarReciboDigitalUseCase;
    private final ListarPedidoPorIdUseCase listarPedidoPorIdUseCase;
    private final AtualizarPedidoUseCase atualizarPedidoUseCase;
    private final ExcluirPedidoUseCase excluirPedidoUseCase;
    private final ContarPorStatusUseCase contarPorStatusUseCase;
    private final ListarProximasRetiradasUseCase listarProximasRetiradasUseCase;
    private final ListarPedidosUseCase listarPedidosUseCase;
    private final ListarItemPedidoPorIdUseCase listarItemPedidoPorIdUseCase;
    private final ListarPagamentosPedidoUseCase listarPagamentosPedidoUseCase;
    private final PedidoPresentationMapper mapper;

    public PedidoController(CriarPedidoInternoUseCase criarPedidoInternoUseCase, AlterarStatusPedidoUseCase alterarStatusPedidoUseCase, GerarReciboDigitalUseCase gerarReciboDigitalUseCase, ListarPedidoPorIdUseCase listarPedidoPorIdUseCase, AtualizarPedidoUseCase atualizarPedidoUseCase, ExcluirPedidoUseCase excluirPedidoUseCase, ContarPorStatusUseCase contarPorStatusUseCase, ListarProximasRetiradasUseCase listarProximasRetiradasUseCase, ListarPedidosUseCase listarPedidosUseCase, ListarItemPedidoPorIdUseCase listarItemPedidoPorIdUseCase, ListarPagamentosPedidoUseCase listarPagamentosPedidoUseCase, PedidoPresentationMapper mapper) {
        this.criarPedidoInternoUseCase = criarPedidoInternoUseCase;
        this.alterarStatusPedidoUseCase = alterarStatusPedidoUseCase;
        this.gerarReciboDigitalUseCase = gerarReciboDigitalUseCase;
        this.listarPedidoPorIdUseCase = listarPedidoPorIdUseCase;
        this.atualizarPedidoUseCase = atualizarPedidoUseCase;
        this.excluirPedidoUseCase = excluirPedidoUseCase;
        this.contarPorStatusUseCase = contarPorStatusUseCase;
        this.listarProximasRetiradasUseCase = listarProximasRetiradasUseCase;
        this.listarPedidosUseCase = listarPedidosUseCase;
        this.listarItemPedidoPorIdUseCase = listarItemPedidoPorIdUseCase;
        this.listarPagamentosPedidoUseCase = listarPagamentosPedidoUseCase;
        this.mapper = mapper;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<PedidoResponseDto> criarPedido(@Valid @RequestBody CriarPedidoRequestDto request,
                                                        @AuthenticationPrincipal UsuarioAutenticado usuario) {
        // 1. Converte DTO HTTP para Command Interno
        CriarPedidoCommand command = mapper.toCommand(usuario.getId(), request);

        // 2. Executa a Regra de Negócio Pura
        Pedido pedidoSalvo = criarPedidoInternoUseCase.executar(command);

        // 3. Converte o resultado de volta para DTO HTTP
        PedidoResponseDto response = mapper.toResponse(pedidoSalvo);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/{id}/status")
    @Transactional
    public ResponseEntity<PedidoResponseDto> alterarStatus(@PathVariable Long id,
                                                           @Valid @RequestBody AlterarStatusRequestDto request,
                                                           @AuthenticationPrincipal UsuarioAutenticado usuario) {
        Pedido pedidoAtualizado = alterarStatusPedidoUseCase.executar(id, request.novoStatus(), usuario.getId(), request.observacao());
        return ResponseEntity.ok(mapper.toResponse(pedidoAtualizado));
    }

    @GetMapping("/{id}")
    @Transactional
    public ResponseEntity<PedidoResponseDto> listarPorId(@PathVariable Long id) {
        Pedido pedido = listarPedidoPorIdUseCase.executar(id);

        PedidoResponseDto response = mapper.toResponse(pedido);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/recibo")
    @Transactional
    public ResponseEntity<String> gerarReciboDigital(@PathVariable Long id) {
        // Retorna o texto formatado para o WhatsApp
        String recibo = gerarReciboDigitalUseCase.executar(id);
        return ResponseEntity.ok(recibo);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<PedidoResponseDto> atualizarPedido(@PathVariable Long id,
                                                             @Valid @RequestBody AtualizarPedidoRequestDto request) {
        // 1. Converte DTO HTTP para Command Interno
        AtualizarPedidoCommand command = mapper.toCommand(id, request);

        // 2. Executa a Regra de Negócio Pura
        Pedido pedidoAtualizado = atualizarPedidoUseCase.executar(command);

        // 3. Converte o resultado de volta para DTO HTTP
        PedidoResponseDto response = mapper.toResponse(pedidoAtualizado);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "cancela pedido (soft delete)", description = "Altera o status para CANCELADO e marca o pedido como deletado logicamente.")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        excluirPedidoUseCase.executar(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/count-by-status")
    public ResponseEntity<?> contarPorStatus() {
        return ResponseEntity.ok(contarPorStatusUseCase.executar());
    }

    @GetMapping("/proximas-retiradas")
    @Transactional
    public ResponseEntity<List<PedidoResponseDto>> listarProximasRetiradas(
            @RequestParam int dias) {

        List<Pedido> pedidos =
                listarProximasRetiradasUseCase.executar(dias);

        List<PedidoResponseDto> response = pedidos.stream()
                .map(mapper::toResponse)
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Transactional
    public ResponseEntity<List<PedidoResponseDto>> listarPedidos(
            @RequestParam(required = false) String busca,
            @RequestParam(required = false) StatusProducaoEnum status,
            @RequestParam(required = false) Long evento) {

        List<Pedido> pedidos = listarPedidosUseCase.executar(
                busca,
                status,
                evento
        );

        List<PedidoResponseDto> response = pedidos.stream()
                .map(mapper::toResponse)
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{pedidoId}/itens/{itemId}")
    @Transactional
    public ResponseEntity<ItemPedidoResponseDto> listarItemPorId(
            @PathVariable Long pedidoId,
            @PathVariable Long itemId) {

        ItemPedido item =
                listarItemPedidoPorIdUseCase.executar(pedidoId, itemId);

        return ResponseEntity.ok(mapper.toResponse(item));
    }

    @GetMapping("/{id}/pagamentos")
    @Transactional
    public ResponseEntity<List<PagamentoResponseDto>> listarPagamentos(
            @PathVariable Long id) {

        List<Pagamento> pagamentos =
                listarPagamentosPedidoUseCase.executar(id);

        List<PagamentoResponseDto> response = pagamentos.stream()
                .map(mapper::toResponse)
                .toList();

        return ResponseEntity.ok(response);
    }
}