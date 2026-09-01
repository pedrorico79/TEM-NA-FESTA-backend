package com.temnafesta.application.dto;

import java.math.BigDecimal;

public record CriarProdutoCommand(
        String nome,
        String descricao,
        BigDecimal precoVenda,
        Boolean ativo
) {}