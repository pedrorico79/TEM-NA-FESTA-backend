package com.temnafesta.presentation.dto;

import java.time.LocalDate;

public record ClienteResponseDto(
        Long id,
        String nome,
        String telefone,
        String whatsapp,
        String instagram,
        LocalDate dataCadastro,
        String anotacoes,
        EnderecoDto endereco,
        boolean ativo
) {}