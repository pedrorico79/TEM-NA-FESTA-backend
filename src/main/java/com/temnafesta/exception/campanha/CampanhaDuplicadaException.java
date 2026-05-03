package com.temnafesta.exception.campanha;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class CampanhaDuplicadaException extends RuntimeException {
    public CampanhaDuplicadaException(String nome) {
        super("Já existe uma campanha: " + nome);
    }
}
