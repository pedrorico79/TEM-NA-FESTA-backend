package com.temnafesta.mapper;

import com.temnafesta.dto.metodoPagamento.MetodoPagamentoResponseDto;
import com.temnafesta.model.MetodoPagamento;

public class MetodoPagamentoMapper {

    private MetodoPagamentoMapper() {
    }

    public static MetodoPagamentoResponseDto toResponseDto(
            MetodoPagamento metodoPagamento
    ) {
        MetodoPagamentoResponseDto dto =
                new MetodoPagamentoResponseDto();

        dto.setId(metodoPagamento.getId());
        dto.setNome(metodoPagamento.getNome());

        return dto;
    }
}
