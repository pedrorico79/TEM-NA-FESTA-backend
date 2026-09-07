package com.temnafesta.application.dto.relatorio;

public record PedidosPorSemanaOutput(
        String rotulo,
        String periodo,
        Long quantidade
) {}
