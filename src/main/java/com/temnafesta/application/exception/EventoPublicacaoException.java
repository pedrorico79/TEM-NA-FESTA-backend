package com.temnafesta.application.exception;

public class EventoPublicacaoException extends RuntimeException {
    public EventoPublicacaoException(String message) {
        super(message);
    }

    public EventoPublicacaoException(String message, Throwable cause) {
        super(message, cause);
    }
}
