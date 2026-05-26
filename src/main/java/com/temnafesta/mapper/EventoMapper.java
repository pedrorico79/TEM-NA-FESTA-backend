package com.temnafesta.mapper;

import com.temnafesta.dto.campanha.CampanhaRequestDto;
import com.temnafesta.dto.campanha.CampanhaResponseDto;
import com.temnafesta.model.Evento;

import java.util.List;

public class EventoMapper {

    private EventoMapper(){

    }

    public static Evento toEntityForCreate(CampanhaRequestDto dto) {
        Evento evento = new Evento();
        evento.setNome(dto.getNome());
        evento.setDataInicio(dto.getDataInicio());
        evento.setDataFim(dto.getDataFim());
        evento.setAtiva(false); // sempre começa inativa
        return evento;
    }
    public static Evento toEntityForUpdate(CampanhaRequestDto dto) {
        Evento evento = new Evento();
        evento.setNome(dto.getNome());
        evento.setDataInicio(dto.getDataInicio());
        evento.setDataFim(dto.getDataFim());
        evento.setAtiva(dto.getAtiva()); // pode ser true ou false
        return evento;
    }



    public static CampanhaResponseDto toResponse (Evento evento){
        CampanhaResponseDto dto = new CampanhaResponseDto();
        dto.setId(evento.getId());
        dto.setNome(evento.getNome());
        dto.setDataInicio(evento.getDataInicio());
        dto.setDataFim(evento.getDataFim());
        dto.setAtiva(evento.getAtiva());
        return dto;
    }

    public static List<CampanhaResponseDto> toResponseDto(List<Evento> eventos){
        return eventos.stream()
                .map(EventoMapper::toResponse)
                .toList();
    }
}
