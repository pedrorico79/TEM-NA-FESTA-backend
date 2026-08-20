package com.temnafesta.application.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record CriarPedidoCommand(
        Long clienteId,
        Long usuarioId,
        LocalDateTime dataEntrega,
        BigDecimal taxaEntrega,
        String observacao,
        Long eventoId,
        Long enderecoEntregaId,
        List<ItemCommand> itens
) {
    public record ItemCommand(
            Long produtoId,
            Integer quantidade,
            String observacaoItem
    ) {}
}