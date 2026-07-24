package com.temnafesta.mapper;

import com.temnafesta.dto.lembrete.LembreteRequestDto;
import com.temnafesta.dto.lembrete.LembreteResponseDto;
import com.temnafesta.model.Lembrete;
import com.temnafesta.model.Usuario;

import java.util.List;

public class LembreteMapper {

    public LembreteMapper() {}

    public static Lembrete toEntity(LembreteRequestDto dto) {
        Lembrete lembrete = new Lembrete();
        lembrete.setDescricao(dto.getDescricao());
        lembrete.setData_criacao(dto.getData_criacao());
        lembrete.setData_limite(dto.getData_limite());
        lembrete.setPrioridade(dto.getPrioridade());
        return lembrete;
    }

    public static LembreteResponseDto toResponse(Lembrete lembrete) {

        LembreteResponseDto dto = new LembreteResponseDto();

        dto.setId(lembrete.getId());
        dto.setDescricao(lembrete.getDescricao());
        dto.setDataCriacao(lembrete.getData_criacao());
        dto.setDataLimite(lembrete.getData_limite());
        dto.setPrioridade(lembrete.getPrioridade());
        return dto;
    }

    public static List<LembreteResponseDto> toResponseList(List<Lembrete> lembretes) {
        return lembretes.stream()
                .map(LembreteMapper::toResponse)
                .toList();
    }
}
