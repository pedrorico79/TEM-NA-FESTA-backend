package com.temnafesta.application.exception;

public class SemPermissaoException extends RuntimeException {

    public SemPermissaoException(String mensagem) {
        super(mensagem);
    }
}
