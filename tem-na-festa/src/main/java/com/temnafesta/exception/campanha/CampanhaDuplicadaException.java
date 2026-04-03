package com.temnafesta.exception.campanha;

public class CampanhaDuplicadaException extends RuntimeException {
    public CampanhaDuplicadaException(String nome) {
        super("Já existe uma campanha: " + nome);
    }
}
