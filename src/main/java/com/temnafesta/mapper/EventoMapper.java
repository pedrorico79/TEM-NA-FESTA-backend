package com.temnafesta.mapper;

import com.temnafesta.dto.evento.EventoRequestDto;
import com.temnafesta.dto.evento.EventoResponseDto;
import com.temnafesta.model.Evento;

import java.util.List;

public class EventoMapper {

    private EventoMapper(){

    }

    public static Evento toEntityForCreate(EventoRequestDto dto) {
        Evento evento = new Evento();
        evento.setNome(dto.getNome());
        evento.setDataInicio(dto.getDataInicio());
        evento.setDataFim(dto.getDataFim());
        evento.setIsAtivo(false); // sempre começa inativa
        return evento;
    }
    public static Evento toEntityForUpdate(EventoRequestDto dto) {
        Evento evento = new Evento();
        evento.setNome(dto.getNome());
        evento.setDataInicio(dto.getDataInicio());
        evento.setDataFim(dto.getDataFim());
        evento.setIsAtivo(dto.getAtiva()); // pode ser true ou false
        return evento;
    }



    public static EventoResponseDto toResponse (Evento evento){
        EventoResponseDto dto = new EventoResponseDto();
        dto.setId(evento.getId());
        dto.setNome(evento.getNome());
        dto.setDataInicio(evento.getDataInicio());
        dto.setDataFim(evento.getDataFim());
        dto.setAtiva(evento.getIsAtivo());
        dto.setIsDeletado(evento.getIsDeletado());
        return dto;
    }

    public static List<EventoResponseDto> toResponseDto(List<Evento> eventos){
        return eventos.stream()
                .map(EventoMapper::toResponse)
                .toList();
    }
}
