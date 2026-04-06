package com.temnafesta.exception.pedido;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PedidoNaoEncontrado extends RuntimeException {
    public PedidoNaoEncontrado(Integer id) {
        super("Pedido com id %d não encontrado".formatted(id));
    }
}
