package com.temnafesta.mapper;

import com.temnafesta.dto.perfil.PerfilRequestDto;
import com.temnafesta.dto.perfil.PerfilResponseDto;
import com.temnafesta.model.Perfil;

import java.util.List;

public class PerfilMapper {
    public static Perfil toEntity(PerfilRequestDto dto) {
        Perfil perfil = new Perfil();
        perfil.setNome(dto.getNome());
        return perfil;
    }

    public static PerfilResponseDto toResponseDto(Perfil perfil) {

        PerfilResponseDto dto = new PerfilResponseDto();
        dto.setId(perfil.getId());
        dto.setNome(perfil.getNome());

        return dto;
    }

    public static List<PerfilResponseDto> toResponseDtoList(List<Perfil> perfis) {
        return perfis.stream()
                .map(PerfilMapper::toResponseDto)
                .toList();
    }
}
