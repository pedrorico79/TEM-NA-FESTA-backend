package com.temnafesta.presentation.dto;

import java.time.LocalDate;

public record EventoResponseDto(
        Long id,
        String nome,
        LocalDate dataInicio,
        LocalDate dataFim,
        boolean ativo,
        boolean deletado
) {}