package com.temnafesta.presentation.mapper;

import com.temnafesta.application.dto.CriarProdutoCommand;
import com.temnafesta.domain.model.Produto;
import com.temnafesta.presentation.dto.CriarProdutoRequestDto;
import com.temnafesta.presentation.dto.ProdutoResponseDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProdutoPresentationMapper {

    CriarProdutoCommand toCommand(CriarProdutoRequestDto dto);

    ProdutoResponseDto toResponse(Produto domain);
}
