package com.temnafesta.presentation.dto;

import java.util.List;

public record EvolucaoEventoResponseDto(
        String agrupamento,
        List<EvolucaoEventoPeriodoResponseDto> dados
) {}
