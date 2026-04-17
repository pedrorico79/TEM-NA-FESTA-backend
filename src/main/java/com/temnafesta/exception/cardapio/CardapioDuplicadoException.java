package com.temnafesta.exception.cardapio;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class CardapioDuplicadoException extends RuntimeException {
    public CardapioDuplicadoException(String nome) {
        super("Já existe um cardápio com o nome: " + nome);
    }
}
