package com.temnafesta.presentation.controller;

import com.temnafesta.application.usecase.RegistrarPagamentoUseCase;
import com.temnafesta.domain.model.Pedido;
import com.temnafesta.presentation.dto.PedidoResponseDto;
import com.temnafesta.presentation.dto.RegistrarPagamentoRequestDto;
import com.temnafesta.presentation.mapper.PedidoPresentationMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/pedidos/{pedidoId}/pagamentos")
public class PagamentoController {

    private final RegistrarPagamentoUseCase registrarPagamentoUseCase;
    private final PedidoPresentationMapper mapper;

    public PagamentoController(RegistrarPagamentoUseCase registrarPagamentoUseCase,
                               PedidoPresentationMapper mapper) {
        this.registrarPagamentoUseCase = registrarPagamentoUseCase;
        this.mapper = mapper;
    }

    @PostMapping
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
}