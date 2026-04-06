package com.temnafesta.exception.usuario;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UsuarioNaoEncontrado extends RuntimeException {
    public UsuarioNaoEncontrado(Integer id) {
        super("Usuario com id %d não encontrado".formatted(id));
    }
}
