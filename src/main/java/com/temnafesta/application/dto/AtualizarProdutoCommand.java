package com.temnafesta.application.dto;

import java.math.BigDecimal;

public record AtualizarProdutoCommand(
        Long id,
        String nome,
        String descricao,
        BigDecimal precoVenda,
        Boolean ativo
) {}
