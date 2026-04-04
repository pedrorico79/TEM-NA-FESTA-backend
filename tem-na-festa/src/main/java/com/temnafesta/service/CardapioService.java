package com.temnafesta.service;

import com.temnafesta.dto.cardapio.CardapioRequestDto;
import com.temnafesta.exception.cardapio.CardapioDuplicadoException;
import com.temnafesta.exception.cardapio.CardapioNaoEncontrado;
import com.temnafesta.mapper.CardapioMapper;
import com.temnafesta.model.Campanha;
import com.temnafesta.model.Cardapio;
import com.temnafesta.repository.CardapioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardapioService {

    private final CardapioRepository cardapioRepository;
    private final CampanhaService campanhaService;

    public CardapioService(CardapioRepository cardapioRepository,
                           CampanhaService campanhaService) {
        this.cardapioRepository = cardapioRepository;
        this.campanhaService = campanhaService;
    }

    public List<Cardapio> findAll() {
        return cardapioRepository.findAll();
    }

    public Cardapio findById(Integer id) {
        return cardapioRepository.findById(id)
                .orElseThrow(() -> new CardapioNaoEncontrado(id));
    }

    public Cardapio create(CardapioRequestDto dto) {
        cardapioRepository.findByNomeIgnoreCase(dto.getNome())
                .ifPresent(c -> { throw new CardapioDuplicadoException(dto.getNome()); });

        Campanha campanha = campanhaService.findById(dto.getCampanhaId());
        Cardapio cardapio = CardapioMapper.toEntityForCreate(dto, campanha);
        return cardapioRepository.save(cardapio);
    }

    public Cardapio update(Integer id, CardapioRequestDto dto) {
        Cardapio existente = cardapioRepository.findById(id)
                .orElseThrow(() -> new CardapioNaoEncontrado(id));

        if (cardapioRepository.existsByNomeIgnoreCaseAndIdNot(dto.getNome(), id)) {
            throw new CardapioDuplicadoException(dto.getNome());
        }

        Campanha campanha = campanhaService.findById(dto.getCampanhaId());

        Cardapio atualizado = CardapioMapper.toEntityForUpdate(dto, campanha, existente.getAtivo());
        atualizado.setId(id);

        return cardapioRepository.save(atualizado);
    }


    public void deleteById(Integer id) {
        if (!cardapioRepository.existsById(id)) {
            throw new CardapioNaoEncontrado(id);
        }
        cardapioRepository.deleteById(id);
    }

    public List<Cardapio> findByisAtivo(Boolean filtro) {
        return cardapioRepository.findByIsAtivo(filtro);
    }

    public Cardapio updateAtivo(Integer id, Boolean ativo) {
        Cardapio existente = cardapioRepository.findById(id)
                .orElseThrow(() -> new CardapioNaoEncontrado(id));
        existente.setAtivo(ativo);
        return cardapioRepository.save(existente);
    }
}