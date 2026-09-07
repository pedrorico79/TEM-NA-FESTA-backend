package com.temnafesta.presentation.mapper;

import com.temnafesta.application.dto.AlterarAtivoClienteCommand;
import com.temnafesta.application.dto.AtualizarClienteCommand;
import com.temnafesta.application.dto.CriarClienteCommand;
import com.temnafesta.domain.model.Cliente;
import com.temnafesta.presentation.dto.AlterarAtivoClienteRequestDto;
import com.temnafesta.presentation.dto.AtualizarClienteRequestDto;
import com.temnafesta.presentation.dto.ClienteResponseDto;
import com.temnafesta.presentation.dto.CriarClienteRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ClientePresentationMapper {
    CriarClienteCommand toCommand(CriarClienteRequestDto dto);

    @Mapping(target = "id", source = "clienteIdUrl")
    AtualizarClienteCommand toCommand(AtualizarClienteRequestDto dto, Long clienteIdUrl);

    @Mapping(target = "id", source = "clienteIdUrl")
    AlterarAtivoClienteCommand toCommand(AlterarAtivoClienteRequestDto dto, Long clienteIdUrl);

    ClienteResponseDto toResponse(Cliente domain);

}