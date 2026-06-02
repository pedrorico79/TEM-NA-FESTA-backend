package com.temnafesta.exception.evento;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class EventoDuplicadoException extends RuntimeException {
    public EventoDuplicadoException(String nome) {
        super("Já existe uma campanha: " + nome);
    }
}
