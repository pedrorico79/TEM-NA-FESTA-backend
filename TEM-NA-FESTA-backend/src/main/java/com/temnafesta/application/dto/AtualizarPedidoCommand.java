package com.temnafesta.application.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record AtualizarPedidoCommand(
        Long pedidoId,
        LocalDateTime dataEntrega,
        BigDecimal taxaEntrega,
        String observacao,
        Long enderecoEntregaId,
        List<ItemCommand> itens
) {
    public record ItemCommand(
            Long produtoId,
            Integer quantidade,
            BigDecimal precoUnitario,
            String observacaoItem
    ) {}
}