package com.temnafesta.exception.usuario;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class UsuarioJaExiste extends RuntimeException {
    public UsuarioJaExiste(String email) {
        super("Já existe um usuário com o email: " + email);
    }
}
