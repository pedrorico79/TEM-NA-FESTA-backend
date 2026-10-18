package com.temnafesta.application.dto;

import com.temnafesta.domain.model.Endereco;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record AtualizarPedidoCommand(
        Long pedidoId,
        LocalDateTime dataEntrega,
        BigDecimal taxaEntrega,
        BigDecimal descontoPedido,
        String observacao,
        Long eventoId,
        Endereco enderecoEntrega,
        List<ItemCommand> itens
) {
    public AtualizarPedidoCommand(Long pedidoId, LocalDateTime dataEntrega, BigDecimal taxaEntrega,
                                  String observacao, Long eventoId, Endereco enderecoEntrega,
                                  List<ItemCommand> itens) {
        this(pedidoId, dataEntrega, taxaEntrega, null,
                observacao, eventoId, enderecoEntrega, itens);
    }

    public record ItemCommand(
            Long itemId,
            Long produtoId,
            Integer quantidade,
            BigDecimal precoUnitario,
            BigDecimal acrescimo,
            String observacaoItem
    ) {
        public ItemCommand(Long produtoId, Integer quantidade, BigDecimal precoUnitario,
                           String observacaoItem) {
            this(null, produtoId, quantidade, precoUnitario, BigDecimal.ZERO, observacaoItem);
        }
    }
}
