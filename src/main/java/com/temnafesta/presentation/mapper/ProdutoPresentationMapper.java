package com.temnafesta.presentation.mapper;

import com.temnafesta.domain.model.Produto;
import com.temnafesta.presentation.dto.ProdutoResponseDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProdutoPresentationMapper {

    ProdutoResponseDto toResponse(Produto domain);
}
