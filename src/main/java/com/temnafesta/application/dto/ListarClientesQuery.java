package com.temnafesta.application.dto;

public record ListarClientesQuery(
        String termoBusca, // Pode ser parte do nome ou telefone
        int pagina,
        int tamanho
) {}
