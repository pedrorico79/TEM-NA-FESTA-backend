package com.temnafesta.application.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

class RecursoNaoEncontradoExceptionTest {

    @Test
    void devePreservarMensagem() {
        RecursoNaoEncontradoException ex =
                new RecursoNaoEncontradoException("Cliente não encontrado com o ID: 1");

        assertEquals("Cliente não encontrado com o ID: 1", ex.getMessage());
        assertInstanceOf(RuntimeException.class, ex);
    }
}
