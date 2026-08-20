package com.temnafesta.presentation.mapper;

import com.temnafesta.application.dto.CriarPedidoCommand;
import com.temnafesta.domain.model.Pedido;
import com.temnafesta.presentation.dto.CriarPedidoRequestDto;
import com.temnafesta.presentation.dto.ItemPedidoRequestDto;
import com.temnafesta.presentation.dto.PedidoResponseDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PedidoPresentationMapper {

    // Converte o DTO da Web para o Command do Use Case
    CriarPedidoCommand toCommand(CriarPedidoRequestDto dto);

    CriarPedidoCommand.ItemCommand toCommand(ItemPedidoRequestDto dto);

    // Converte a Entidade de Domínio para o DTO de Saída (Response)
    PedidoResponseDto toResponse(Pedido domain);
}