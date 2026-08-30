package com.temnafesta.application.dto;

public record AlterarAtivoProdutoCommand(
        Long id,
        boolean ativo
) {}
