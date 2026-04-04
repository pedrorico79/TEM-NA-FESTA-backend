package com.temnafesta.mapper;

import com.temnafesta.dto.cardapioproduto.CardapioProdutoRequestDto;
import com.temnafesta.dto.cardapioproduto.CardapioProdutoResponseDto;
import com.temnafesta.model.CardapioProduto;

import java.util.List;

public class CardapioProdutoMapper {

    private CardapioProdutoMapper() {
    }

    public static CardapioProduto toEntity(CardapioProdutoRequestDto dto) {
        CardapioProduto entity = new CardapioProduto();
        entity.setOrdemExibicao(dto.getOrdemExibicao());
        entity.setQtdProdutoTotal(dto.getQtdProdutoTotal());
        entity.setQtdProdutoDisponivel(dto.getQtdProdutoDisponivel());
        return entity;
    }

    public static CardapioProdutoResponseDto toResponseDto(CardapioProduto entity) {
        CardapioProdutoResponseDto.CardapioDto cardapioDto = new CardapioProdutoResponseDto.CardapioDto();
        cardapioDto.setId(entity.getCardapio().getId());
        cardapioDto.setNome(entity.getCardapio().getNome());

        CardapioProdutoResponseDto.ProdutoDto produtoDto = new CardapioProdutoResponseDto.ProdutoDto();
        produtoDto.setId(entity.getProduto().getId());

        CardapioProdutoResponseDto dto = new CardapioProdutoResponseDto();
        dto.setId(entity.getId());
        dto.setCardapio(cardapioDto);
        dto.setProduto(produtoDto);
        dto.setOrdemExibicao(entity.getOrdemExibicao());
        dto.setQtdProdutoTotal(entity.getQtdProdutoTotal());
        dto.setQtdProdutoDisponivel(entity.getQtdProdutoDisponivel());

        return dto;
    }

    public static List<CardapioProdutoResponseDto> toResponseDtoList(List<CardapioProduto> entities) {
        return entities.stream()
                .map(CardapioProdutoMapper::toResponseDto)
                .toList();
    }
}