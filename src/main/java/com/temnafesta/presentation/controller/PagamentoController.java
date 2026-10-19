package com.temnafesta.presentation.controller;

import com.temnafesta.application.usecase.ConfirmarPagamentoUseCase;
import com.temnafesta.application.usecase.ExcluirPagamentoUseCase;
import com.temnafesta.application.usecase.RegistrarPagamentoUseCase;
import com.temnafesta.domain.model.Pedido;
import com.temnafesta.infrastructure.security.user.UsuarioAutenticado;
import com.temnafesta.presentation.dto.PedidoResponseDto;
import com.temnafesta.presentation.dto.RegistrarPagamentoRequestDto;
import com.temnafesta.presentation.mapper.PedidoPresentationMapper;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/pedidos/{pedidoId}/pagamentos")
public class PagamentoController {

    private final RegistrarPagamentoUseCase registrarPagamentoUseCase;
    private final ExcluirPagamentoUseCase excluirPagamentoUseCase;
    private final ConfirmarPagamentoUseCase confirmarPagamentoUseCase;
    private final PedidoPresentationMapper mapper;

    public PagamentoController(RegistrarPagamentoUseCase registrarPagamentoUseCase,
                               ExcluirPagamentoUseCase excluirPagamentoUseCase,
                               ConfirmarPagamentoUseCase confirmarPagamentoUseCase,
                               PedidoPresentationMapper mapper) {
        this.registrarPagamentoUseCase = registrarPagamentoUseCase;
        this.excluirPagamentoUseCase = excluirPagamentoUseCase;
        this.confirmarPagamentoUseCase = confirmarPagamentoUseCase;
        this.mapper = mapper;
    }

    @PostMapping
    @Operation(summary = "Registra um pagamento em um pedido",
            description = "O autor é extraído do token autenticado. O status pode ser CONFIRMADO ou PENDENTE; se omitido, será CONFIRMADO.")
    public ResponseEntity<PedidoResponseDto> registrarPagamento(
            @PathVariable Long pedidoId,
            @RequestHeader(value = "Idempotency-Key", required = false) String chaveIdempotencia,
            @Valid @RequestBody RegistrarPagamentoRequestDto request,
            @AuthenticationPrincipal UsuarioAutenticado usuario) {

        // Idempotência: se o cliente não enviar a chave, gera uma para esta requisição.
        // Reenvios com a mesma chave retornam o pagamento original sem duplicar.
        String chave = (chaveIdempotencia == null || chaveIdempotencia.isBlank())
                ? UUID.randomUUID().toString()
                : chaveIdempotencia;

        Pedido pedidoAtualizado = registrarPagamentoUseCase.executar(
                pedidoId,
                request.valor(),
                request.tipoPagamento(),
                request.metodoPagamentoId(),
                usuario.getId(),
                request.statusPagamento(),
                chave
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(pedidoAtualizado));
    }

    @PatchMapping("/{pagamentoId}/confirmar")
    @Operation(summary = "Confirma um pagamento pendente")
    public ResponseEntity<PedidoResponseDto> confirmarPagamento(
            @PathVariable Long pedidoId,
            @PathVariable Long pagamentoId) {
        Pedido pedidoAtualizado = confirmarPagamentoUseCase.executar(pedidoId, pagamentoId);
        return ResponseEntity.ok(mapper.toResponse(pedidoAtualizado));
    }

    @DeleteMapping("/{pagamentoId}")
    @Operation(summary = "Cancela um pagamento de um pedido", description = "Marca o pagamento como cancelado e preserva seu histórico.")
    public ResponseEntity<Void> excluirPagamento(
            @PathVariable Long pedidoId,
            @PathVariable Long pagamentoId) {

        excluirPagamentoUseCase.executar(pedidoId, pagamentoId);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{pagamentoId}/cancelar")
    @Operation(summary = "Cancela um pagamento de um pedido", description = "Marca o pagamento como cancelado e preserva seu histórico.")
    public ResponseEntity<PedidoResponseDto> cancelarPagamento(
            @PathVariable Long pedidoId,
            @PathVariable Long pagamentoId) {
        Pedido pedidoAtualizado = excluirPagamentoUseCase.executar(pedidoId, pagamentoId);
        return ResponseEntity.ok(mapper.toResponse(pedidoAtualizado));
    }
}
