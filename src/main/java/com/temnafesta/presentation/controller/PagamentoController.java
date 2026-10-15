package com.temnafesta.presentation.controller;

import com.temnafesta.application.usecase.ExcluirPagamentoUseCase;
import com.temnafesta.application.usecase.RegistrarPagamentoUseCase;
import com.temnafesta.application.usecase.ConfirmarPagamentoUseCase;
import com.temnafesta.domain.model.Pedido;
import com.temnafesta.domain.vo.StatusPagamentoEnum;
import com.temnafesta.infrastructure.security.user.UsuarioAutenticado;
import com.temnafesta.presentation.dto.PedidoResponseDto;
import com.temnafesta.presentation.dto.RegistrarPagamentoRequestDto;
import com.temnafesta.presentation.mapper.PedidoPresentationMapper;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.web.bind.annotation.*;

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
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public ResponseEntity<PedidoResponseDto> registrarPagamento(
            @PathVariable Long pedidoId,
            @RequestHeader("Idempotency-Key") String chaveIdempotencia,
            @Valid @RequestBody RegistrarPagamentoRequestDto request,
            @AuthenticationPrincipal UsuarioAutenticado usuario) {

        // Executa a regra que adiciona o pagamento e verifica se o pedido muda para CONFIRMADO
        Pedido pedidoAtualizado = registrarPagamentoUseCase.executar(
                pedidoId,
                request.valor(),
                request.tipoPagamento(),
                request.metodoPagamentoId(),
                usuario.getId(),
                request.statusPagamento() == null ? StatusPagamentoEnum.CONFIRMADO : request.statusPagamento(),
                chaveIdempotencia
        );

        // Retornamos o estado atualizado do pedido (com totais e novo status)
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(pedidoAtualizado));
    }

    @PatchMapping("/{pagamentoId}/confirmar")
    @Transactional
    public ResponseEntity<PedidoResponseDto> confirmarPagamento(
            @PathVariable Long pedidoId,
            @PathVariable Long pagamentoId) {
        Pedido pedidoAtualizado = confirmarPagamentoUseCase.executar(pedidoId, pagamentoId);
        return ResponseEntity.ok(mapper.toResponse(pedidoAtualizado));
    }

    @DeleteMapping("/{pagamentoId}")
    @Transactional
    @Operation(summary = "Cancela um pagamento de um pedido", description = "Marca o pagamento como cancelado e preserva seu histórico.")
    public ResponseEntity<Void> excluirPagamento(
            @PathVariable Long pedidoId,
            @PathVariable Long pagamentoId) {

        excluirPagamentoUseCase.executar(pedidoId, pagamentoId);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{pagamentoId}/cancelar")
    @Transactional
    public ResponseEntity<PedidoResponseDto> cancelarPagamento(
            @PathVariable Long pedidoId,
            @PathVariable Long pagamentoId) {
        Pedido pedidoAtualizado = excluirPagamentoUseCase.executar(pedidoId, pagamentoId);
        return ResponseEntity.ok(mapper.toResponse(pedidoAtualizado));
    }

}
