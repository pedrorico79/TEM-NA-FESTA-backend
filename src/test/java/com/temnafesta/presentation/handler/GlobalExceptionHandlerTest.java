package com.temnafesta.presentation.handler;

import com.temnafesta.application.exception.RecursoNaoEncontradoException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void deveRetornarNotFoundParaRecursoNaoEncontrado() {
        RecursoNaoEncontradoException exception = new RecursoNaoEncontradoException(
                "Produto não encontrado com o ID: 1");

        ResponseEntity<ErroResponse> response = handler.handleRecursoNaoEncontradoException(exception);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(404, response.getBody().status());
        assertEquals("Recurso Não Encontrado", response.getBody().erro());
    }
}
