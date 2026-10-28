package com.temnafesta.application.exception;

import com.temnafesta.domain.exception.NaoEncontradoException;

/**
 * Remover assim que não tiver mais outras classes utilizando essa (atualmente apenas tests)
 */
@Deprecated(forRemoval = true)
public class RecursoNaoEncontradoException extends NaoEncontradoException {

    public RecursoNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}
