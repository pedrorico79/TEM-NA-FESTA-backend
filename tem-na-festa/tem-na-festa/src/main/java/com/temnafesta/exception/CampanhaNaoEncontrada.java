package com.temnafesta.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CampanhaNaoEncontrada extends RuntimeException {
    public CampanhaNaoEncontrada(String message) {
        super(message);
    }
}
