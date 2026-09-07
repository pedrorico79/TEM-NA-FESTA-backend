package com.temnafesta.presentation.controller;

import com.temnafesta.application.usecase.ExcluirPagamentoUseCase;
import com.temnafesta.application.usecase.RegistrarPagamentoUseCase;
import com.temnafesta.domain.model.Pedido;
import com.temnafesta.presentation.dto.PedidoResponseDto;
import com.temnafesta.presentation.dto.RegistrarPagamentoRequestDto;
import com.temnafesta.presentation.mapper.PedidoPresentationMapper;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/pedidos/{pedidoId}/pagamentos")
public class PagamentoController {

    private final RegistrarPagamentoUseCase registrarPagamentoUseCase;
    private final ExcluirPagamentoUseCase excluirPagamentoUseCase;
    private final PedidoPresentationMapper mapper;

    public PagamentoController(RegistrarPagamentoUseCase registrarPagamentoUseCase,
                               ExcluirPagamentoUseCase excluirPagamentoUseCase,
                               PedidoPresentationMapper mapper) {
        this.registrarPagamentoUseCase = registrarPagamentoUseCase;
        this.excluirPagamentoUseCase = excluirPagamentoUseCase;
        this.mapper = mapper;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<PedidoResponseDto> registrarPagamento(
            @PathVariable Long pedidoId,
            @Valid @RequestBody RegistrarPagamentoRequestDto request) {

        // Executa a regra que adiciona o pagamento e verifica se o pedido muda para CONFIRMADO
        Pedido pedidoAtualizado = registrarPagamentoUseCase.executar(
                pedidoId,
                request.valor(),
                request.tipoPagamento(),
                request.metodoPagamentoId(),
                request.usuarioId()
        );

        // Retornamos o estado atualizado do pedido (com totais e novo status)
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(pedidoAtualizado));
    }

    @DeleteMapping("/{pagamentoId}")
    @Transactional
    @Operation(summary = "Exclui um pagamento de um pedido", description = "Remove definitivamente (hard delete) um pagamento vinculado ao pedido.")
    public ResponseEntity<Void> excluirPagamento(
            @PathVariable Long pedidoId,
            @PathVariable Long pagamentoId) {

        excluirPagamentoUseCase.executar(pedidoId, pagamentoId);

        return ResponseEntity.noContent().build();
    }
}