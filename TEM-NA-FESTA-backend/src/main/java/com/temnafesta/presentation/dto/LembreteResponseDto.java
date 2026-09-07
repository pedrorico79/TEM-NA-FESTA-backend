package com.temnafesta.presentation.dto;

import java.time.LocalDate;

public record LembreteResponseDto(
        Long id,
        String descricao,
        LocalDate dataCriacao,
        LocalDate dataLimite,
        Long usuarioId
) {}