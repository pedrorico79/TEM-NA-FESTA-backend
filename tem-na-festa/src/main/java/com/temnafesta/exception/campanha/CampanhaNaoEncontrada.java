package com.temnafesta.exception.campanha;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CampanhaNaoEncontrada extends RuntimeException {
    public CampanhaNaoEncontrada(Integer id) {
        super("Não existe uma campanha com o id " + id);
    }
}
