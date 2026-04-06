package com.temnafesta.mapper;

import com.temnafesta.dto.campanha.CampanhaRequestDto;
import com.temnafesta.dto.campanha.CampanhaResponseDto;
import com.temnafesta.model.Campanha;

import java.util.List;

public class CampanhaMapper {

    private CampanhaMapper(){

    }

    public static Campanha toEntityForCreate(CampanhaRequestDto dto) {
        Campanha campanha = new Campanha();
        campanha.setNome(dto.getNome());
        campanha.setDataInicio(dto.getDataInicio());
        campanha.setDataFim(dto.getDataFim());
        campanha.setAtiva(false); // sempre começa inativa
        return campanha;
    }
    public static Campanha toEntityForUpdate(CampanhaRequestDto dto) {
        Campanha campanha = new Campanha();
        campanha.setNome(dto.getNome());
        campanha.setDataInicio(dto.getDataInicio());
        campanha.setDataFim(dto.getDataFim());
        campanha.setAtiva(dto.getAtiva()); // pode ser true ou false
        return campanha;
    }



    public static CampanhaResponseDto toResponse (Campanha campanha){
        CampanhaResponseDto dto = new CampanhaResponseDto();
        dto.setId(campanha.getId());
        dto.setNome(campanha.getNome());
        dto.setDataInicio(campanha.getDataInicio());
        dto.setDataFim(campanha.getDataFim());
        dto.setAtiva(campanha.getAtiva());
        return dto;
    }

    public static List<CampanhaResponseDto> toResponseDto(List<Campanha> campanhas){
        return campanhas.stream()
                .map(CampanhaMapper::toResponse)
                .toList();
    }
}
