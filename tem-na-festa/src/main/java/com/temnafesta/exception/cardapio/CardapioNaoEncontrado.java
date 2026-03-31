package com.temnafesta.exception.cardapio;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CardapioNaoEncontrado extends RuntimeException {
    public CardapioNaoEncontrado(String message) {
        super(message);
    }
}
