package com.temnafesta.application.dto;

import com.temnafesta.domain.model.Endereco;
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
        Endereco enderecoEntrega,
        List<ItemCommand> itens
) {
    public record ItemCommand(
            Long produtoId,
            Integer quantidade,
            BigDecimal precoUnitario,
            String observacaoItem
    ) {}
}