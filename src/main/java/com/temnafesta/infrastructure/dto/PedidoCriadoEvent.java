package com.temnafesta.infrastructure.dto;

import com.temnafesta.domain.model.ItemPedido;
import com.temnafesta.domain.model.Pagamento;
import com.temnafesta.domain.vo.StatusProducaoEnum;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record PedidoCriadoEvent(
        Long id,
        LocalDateTime dataPedido,
        LocalDateTime dataEntrega,
        BigDecimal valorTotal,
        BigDecimal taxaEntrega,
        String observacao,
        StatusProducaoEnum statusProducao,
        Long clienteId,
        Long usuarioId,
        Long eventoId,
        Long enderecoEntregaId,
        List<ItemPedido> itens,
        List<Pagamento> pagamentos,
        boolean ativo,
        boolean deletado
) {
}