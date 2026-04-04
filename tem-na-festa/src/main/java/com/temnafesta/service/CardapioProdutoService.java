package com.temnafesta.service;

import com.temnafesta.exception.cardapio.CardapioNaoEncontrado;
import com.temnafesta.exception.cardapioproduto.CardapioProdutoNaoEncontrado;
import com.temnafesta.exception.produto.ProdutoNaoEncontrado;
import com.temnafesta.model.Cardapio;
import com.temnafesta.model.CardapioProduto;
import com.temnafesta.model.Produto;
import com.temnafesta.repository.CardapioRepository;
import com.temnafesta.repository.CardapioProdutoRepository;
import com.temnafesta.repository.ProdutoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardapioProdutoService {

    private final CardapioProdutoRepository cardapioProdutoRepository;
    private final CardapioRepository cardapioRepository;
    private final ProdutoRepository produtoRepository;

    public CardapioProdutoService(CardapioProdutoRepository cardapioProdutoRepository,
                                  CardapioRepository cardapioRepository,
                                  ProdutoRepository produtoRepository) {
        this.cardapioProdutoRepository = cardapioProdutoRepository;
        this.cardapioRepository = cardapioRepository;
        this.produtoRepository = produtoRepository;
    }

    public CardapioProduto criar(CardapioProduto cardapioProduto, Integer cardapioId, Integer produtoId) {
        Cardapio cardapio = cardapioRepository.findById(cardapioId)
                .orElseThrow(() -> new CardapioNaoEncontrado(cardapioId));
        Produto produto = produtoRepository.findById(produtoId)
                .orElseThrow(() -> new ProdutoNaoEncontrado("Produto com id %d não encontrado".formatted(produtoId)));
        cardapioProduto.setCardapio(cardapio);
        cardapioProduto.setProduto(produto);
        return cardapioProdutoRepository.save(cardapioProduto);
    }

    public List<CardapioProduto> listar() {
        return cardapioProdutoRepository.findAll();
    }

    public List<CardapioProduto> listarPorCardapio(Integer cardapioId) {
        return cardapioProdutoRepository.findByCardapioId(cardapioId);
    }

    public CardapioProduto buscarPorId(Integer id) {
        return cardapioProdutoRepository.findById(id)
                .orElseThrow(() -> new CardapioProdutoNaoEncontrado(id));
    }

    public CardapioProduto atualizar(Integer id, CardapioProduto cardapioProdutoAtualizado, Integer cardapioId, Integer produtoId) {
        if (!cardapioProdutoRepository.existsById(id)) {
            throw new CardapioProdutoNaoEncontrado(id);
        }
        Cardapio cardapio = cardapioRepository.findById(cardapioId)
                .orElseThrow(() -> new CardapioNaoEncontrado(cardapioId));
        Produto produto = produtoRepository.findById(produtoId)
                .orElseThrow(() -> new ProdutoNaoEncontrado("Produto com id %d não encontrado".formatted(produtoId)));
        cardapioProdutoAtualizado.setCardapio(cardapio);
        cardapioProdutoAtualizado.setProduto(produto);
        cardapioProdutoAtualizado.setId(id);
        return cardapioProdutoRepository.save(cardapioProdutoAtualizado);
    }

    public void deletar(Integer id) {
        if (!cardapioProdutoRepository.existsById(id)) {
            throw new CardapioProdutoNaoEncontrado(id);
        }
        cardapioProdutoRepository.deleteById(id);
    }
}