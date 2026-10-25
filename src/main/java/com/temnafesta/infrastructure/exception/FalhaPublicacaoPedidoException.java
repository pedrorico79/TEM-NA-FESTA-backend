package com.temnafesta.infrastructure.exception;

public class FalhaPublicacaoPedidoException extends RuntimeException {
    public FalhaPublicacaoPedidoException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
