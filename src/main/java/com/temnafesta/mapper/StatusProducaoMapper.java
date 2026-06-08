package com.temnafesta.mapper;

import com.temnafesta.dto.statusProducao.StatusProducaoResponseDto;
import com.temnafesta.model.StatusProducao;

public class StatusProducaoMapper {

    private StatusProducaoMapper() {
    }

    public static StatusProducaoResponseDto toResponseDto(
            StatusProducao statusProducao
    ) {

        StatusProducaoResponseDto dto =
                new StatusProducaoResponseDto();

        dto.setId(statusProducao.getId());
        dto.setNome(statusProducao.getNome());

        return dto;
    }
}