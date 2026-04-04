package com.temnafesta.exception.cardapioproduto;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CardapioProdutoNaoEncontrado extends RuntimeException {
    public CardapioProdutoNaoEncontrado(Integer id) {
        super("CardapioProduto com id %d não encontrado".formatted(id));
    }
}