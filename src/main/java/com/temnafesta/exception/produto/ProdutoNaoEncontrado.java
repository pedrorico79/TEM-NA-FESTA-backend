package com.temnafesta.exception.produto;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ProdutoNaoEncontrado extends RuntimeException {
    public ProdutoNaoEncontrado(Integer id) {
        super("Produto com id %d não encontrado".formatted(id));
    }
}
