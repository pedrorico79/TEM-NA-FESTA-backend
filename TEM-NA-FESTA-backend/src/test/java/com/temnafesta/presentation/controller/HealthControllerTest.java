package com.temnafesta.presentation.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class HealthControllerTest {

    private final HealthController healthController = new HealthController();

    @Test
    void deveRetornarStatusUp() {
        var resposta = healthController.health();

        assertEquals(HttpStatus.OK, resposta.getStatusCode());
        assertNotNull(resposta.getBody());
        assertEquals("UP", resposta.getBody().get("status"));
    }
}
