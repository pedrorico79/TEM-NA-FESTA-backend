package com.temnafesta.exception.pedidoproduto;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PedidoProdutoNaoEncontrado extends RuntimeException {
    public PedidoProdutoNaoEncontrado(Integer id) {
        super("PedidoProduto com id %d não encontrado".formatted(id));
    }
}