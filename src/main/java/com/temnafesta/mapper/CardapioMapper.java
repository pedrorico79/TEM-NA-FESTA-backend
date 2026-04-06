package com.temnafesta.mapper;

import com.temnafesta.dto.cardapio.CardapioRequestDto;
import com.temnafesta.dto.cardapio.CardapioResponseDto;
import com.temnafesta.model.Campanha;
import com.temnafesta.model.Cardapio;

import java.util.List;

public class CardapioMapper {

    public static Cardapio toEntityForCreate(CardapioRequestDto dto, Campanha campanha) {
        Cardapio cardapio = new Cardapio();
        cardapio.setNome(dto.getNome());
        cardapio.setObservacoes(dto.getObservacoes());
        cardapio.setAtivo(false); // Cardápio sempre é criado inativo
        cardapio.setCampanha(campanha);
        return cardapio;
    }

    public static Cardapio toEntityForUpdate(CardapioRequestDto dto, Campanha campanha, Boolean isAtivo) {
        Cardapio cardapio = new Cardapio();
        cardapio.setNome(dto.getNome());
        cardapio.setObservacoes(dto.getObservacoes());
        cardapio.setAtivo(isAtivo); // valor vindo do banco, não do cliente
        cardapio.setCampanha(campanha);
        return cardapio;
    }

    public static CardapioResponseDto toResponse(Cardapio cardapio) {
        CardapioResponseDto dto = new CardapioResponseDto();
        dto.setId(cardapio.getId());
        dto.setNome(cardapio.getNome());
        dto.setObservacoes(cardapio.getObservacoes());
        dto.setAtivo(cardapio.getAtivo());
        dto.setCampanhaId(cardapio.getCampanha() != null ? cardapio.getCampanha().getId() : null);
        dto.setCampanhaNome(cardapio.getCampanha() != null ? cardapio.getCampanha().getNome() : null);
        return dto;
    }

    public static List<CardapioResponseDto> toResponseDto(List<Cardapio> cardapios) {
        return cardapios.stream()
                .map(CardapioMapper::toResponse)
                .toList();
    }
}