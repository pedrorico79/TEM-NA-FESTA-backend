package com.temnafesta.mapper;

import com.temnafesta.dto.endereco.EnderecoRequestDto;
import com.temnafesta.dto.endereco.EnderecoResponseDto;
import com.temnafesta.model.Endereco;

import java.util.List;

public class EnderecoMapper {

    private EnderecoMapper(){}

    public static EnderecoResponseDto toResponse(Endereco entity){
        EnderecoResponseDto dto = new EnderecoResponseDto();
        dto.setId(entity.getId());
        dto.setCep(entity.getCep());
        dto.setLogradouro(entity.getLogradouro());
        dto.setNumero(entity.getNumero());
        dto.setComplemento(entity.getComplemento());
        dto.setBairro(entity.getBairro());
        dto.setCidade(entity.getCidade());
        dto.setEstado(entity.getEstado());
        return dto;
    }

    public static List<EnderecoResponseDto> toResponseList(List<Endereco> entities) {
        List<EnderecoResponseDto> listaDto = entities.stream()
                .map(EnderecoMapper::toResponse)
                .toList();
        return listaDto;
    }

    public static Endereco toEntity(EnderecoRequestDto request){
        Endereco entity = new Endereco();
        entity.setCep(request.getCep());
        entity.setLogradouro(request.getLogradouro());
        entity.setComplemento(request.getComplemento());
        entity.setNumero(request.getNumero());
        entity.setBairro(request.getBairro());
        entity.setCidade(request.getCidade());
        entity.setEstado(request.getEstado());
        return entity;
    }
}
