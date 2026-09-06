package com.temnafesta.domain.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

class DomainExceptionsTest {

    @Test
    void regraDeNegocioDevePreservarMensagem() {
        RegraDeNegocioException ex = new RegraDeNegocioException("Saldo insuficiente.");

        assertEquals("Saldo insuficiente.", ex.getMessage());
        assertInstanceOf(RuntimeException.class, ex);
    }

    @Test
    void naoEncontradoDevePreservarMensagem() {
        NaoEncontradoException ex = new NaoEncontradoException("Pedido 42 não encontrado.");

        assertEquals("Pedido 42 não encontrado.", ex.getMessage());
        assertInstanceOf(RuntimeException.class, ex);
    }
}
