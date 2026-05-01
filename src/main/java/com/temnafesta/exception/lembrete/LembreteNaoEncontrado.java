package com.temnafesta.exception.lembrete;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class LembreteNaoEncontrado extends RuntimeException {
    public LembreteNaoEncontrado(Integer message) {
        super("Não existe uma campanha com o id " + id);
    }
}
