package com.temnafesta.dto.relatorio.pedidosporsemana;

public record PedidosPorSemanaResponseDto(
        String rotulo,
        String periodo,
        Long quantidade
) {}
