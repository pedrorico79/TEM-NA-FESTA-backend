package com.temnafesta.mapper;

import com.temnafesta.dto.produto.ProdutoRequestDto;
import com.temnafesta.dto.produto.ProdutoResponseDto;
import com.temnafesta.model.Produto;

import java.util.List;

public class ProdutoMapper {
    public static Produto toEntity(ProdutoRequestDto dto) {
        Produto produto = new Produto();
        produto.setNome(dto.getNome());
        produto.setDescricao(dto.getDescricao());
        produto.setPrecoVenda(dto.getPrecoVenda());
        produto.setAtivo(dto.getAtivo());
        return produto;
    }

    public static ProdutoResponseDto toResponseDto(Produto produto) {
        ProdutoResponseDto dto = new ProdutoResponseDto();
        dto.setId(produto.getId());
        dto.setNome(produto.getNome());
        dto.setDescricao(produto.getDescricao());
        dto.setPrecoVenda(produto.getPrecoVenda());
        dto.setAtivo(produto.getAtivo());
        return dto;
    }

    public static List<ProdutoResponseDto> toResponseDtoList(List<Produto> produtos) {
        return produtos.stream()
                .map(ProdutoMapper::toResponseDto)
                .toList();
    }
}
