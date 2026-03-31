package com.temnafesta.exception.pedido;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PedidoNaoEncontrado extends RuntimeException {
    public PedidoNaoEncontrado(String message) {
        super(message);
    }
}
