package com.temnafesta.presentation.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record PedidoResponseDto(
        Long id,
        LocalDateTime dataPedido,
        LocalDateTime dataEntrega,
        BigDecimal valorTotal,
        BigDecimal taxaEntrega,
        String observacao,
        String statusProducao,
        Long clienteId,
        Long usuarioId,
        Long eventoId,
        Long enderecoEntregaId,

        // As listas de filhos aninhadas de forma segura
        List<ItemPedidoResponseDto> itens,
        List<PagamentoResponseDto> pagamentos
) {}