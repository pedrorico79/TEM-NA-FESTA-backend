package com.temnafesta.presentation.mapper;

import com.temnafesta.application.dto.AlterarAtivoProdutoCommand;
import com.temnafesta.application.dto.AtualizarProdutoCommand;
import com.temnafesta.application.dto.CriarProdutoCommand;
import com.temnafesta.domain.model.Produto;
import com.temnafesta.presentation.dto.AlterarAtivoProdutoRequestDto;
import com.temnafesta.presentation.dto.AtualizarProdutoRequestDto;
import com.temnafesta.presentation.dto.CriarProdutoRequestDto;
import com.temnafesta.presentation.dto.ProdutoResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProdutoPresentationMapper {

    CriarProdutoCommand toCommand(CriarProdutoRequestDto dto);

    // produtoIdUrl -> parametro recebido da URL
    // id -> campo do Command
    @Mapping(target = "id", source = "produtoIdUrl")
    AtualizarProdutoCommand toCommand(AtualizarProdutoRequestDto dto, Long produtoIdUrl);

    @Mapping(target = "id", source = "produtoIdUrl")
    AlterarAtivoProdutoCommand toCommand(AlterarAtivoProdutoRequestDto dto, Long produtoIdUrl);

    ProdutoResponseDto toResponse(Produto domain);
}
