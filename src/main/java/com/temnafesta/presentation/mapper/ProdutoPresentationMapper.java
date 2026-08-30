package com.temnafesta.presentation.mapper;

import com.temnafesta.application.dto.AtualizarProdutoCommand;
import com.temnafesta.application.dto.CriarProdutoCommand;
import com.temnafesta.domain.model.Produto;
import com.temnafesta.presentation.dto.AtualizarProdutoRequestDto;
import com.temnafesta.presentation.dto.CriarProdutoRequestDto;
import com.temnafesta.presentation.dto.ProdutoResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProdutoPresentationMapper {

    CriarProdutoCommand toCommand(CriarProdutoRequestDto dto);

    // produtoId -> parametro da URL
    // id -> campo do AtualizarProdutoCommand
    @Mapping(target = "id", source = "produtoId")
    AtualizarProdutoCommand toCommand(AtualizarProdutoRequestDto dto, Long produtoId);

    ProdutoResponseDto toResponse(Produto domain);
}
