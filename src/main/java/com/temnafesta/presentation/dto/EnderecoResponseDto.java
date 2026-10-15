package com.temnafesta.presentation.dto;

public record EnderecoResponseDto(
        Long id,
        String cep,
        String logradouro,
        String numero,
        String complemento,
        String bairro,
        String cidade,
        String estado
) {}
