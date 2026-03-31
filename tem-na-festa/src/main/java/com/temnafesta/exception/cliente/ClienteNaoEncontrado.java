package com.temnafesta.exception.cliente;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ClienteNaoEncontrado extends RuntimeException {
    public ClienteNaoEncontrado(String message) {
        super(message);
    }
}
