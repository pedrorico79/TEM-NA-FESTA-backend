package com.temnafesta.exception.endereco;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class EnderecoNaoEncontrado extends RuntimeException {
    public EnderecoNaoEncontrado(String message) {
        super(message);
    }
}
