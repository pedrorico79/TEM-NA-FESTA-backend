package com.temnafesta.application.exception;

public class FalhaAutenticacaoException extends RuntimeException {
    public FalhaAutenticacaoException() {
        super("E-mail ou senha inválidos.");
    }
}
