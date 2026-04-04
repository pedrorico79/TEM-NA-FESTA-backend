package com.temnafesta.exception.cardapio;

public class CardapioDuplicadoException extends RuntimeException {
    public CardapioDuplicadoException(String nome) {
        super("Já existe um cardápio com o nome: " + nome);
    }
}
