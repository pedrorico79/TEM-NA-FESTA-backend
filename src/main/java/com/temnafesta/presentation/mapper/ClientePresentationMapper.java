package com.temnafesta.presentation.mapper;

import com.temnafesta.application.dto.AtualizarClienteCommand;
import com.temnafesta.application.dto.CriarClienteCommand;
import com.temnafesta.domain.model.Cliente;
import com.temnafesta.presentation.dto.AtualizarClienteRequestDto;
import com.temnafesta.presentation.dto.ClienteResponseDto;
import com.temnafesta.presentation.dto.CriarClienteRequestDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ClientePresentationMapper {
    CriarClienteCommand toCommand(CriarClienteRequestDto dto);
    AtualizarClienteCommand toCommand(AtualizarClienteRequestDto dto);
    ClienteResponseDto toResponse(Cliente domain);

}