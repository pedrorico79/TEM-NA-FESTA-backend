package com.temnafesta.presentation.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * O limite de login por IP usa o IP do cliente informado pelo proxy
 * (X-Forwarded-For), não o IP do proxy. O teste sobe o Tomcat de verdade,
 * porque é ele quem interpreta o cabeçalho; as requisições saem de 127.0.0.1,
 * que conta como proxy de confiança.
 */
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {
                "server.forward-headers-strategy=native",
                "app.login.max-por-conta=5",
                "app.login.max-por-ip=2"
        })
class LoginIpDoClienteTest {

    @Autowired
    private TestRestTemplate http;

    private HttpStatus loginComSenhaErrada(String email, String xForwardedFor) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (xForwardedFor != null) {
            headers.set("X-Forwarded-For", xForwardedFor);
        }
        ResponseEntity<String> resposta = http.postForEntity("/api/v1/auth/login",
                new HttpEntity<>(Map.of("email", email, "senha", "senha-errada"), headers), String.class);
        return HttpStatus.valueOf(resposta.getStatusCode().value());
    }

    @Test
    void bloqueioPorIpValeParaOIpDoClienteENaoParaODoProxy() {
        // Duas falhas do cliente 203.0.113.5, atrás de um proxy interno (10.0.0.7).
        assertEquals(HttpStatus.UNAUTHORIZED, loginComSenhaErrada("a@email.com", "203.0.113.5, 10.0.0.7"));
        assertEquals(HttpStatus.UNAUTHORIZED, loginComSenhaErrada("b@email.com", "203.0.113.5, 10.0.0.7"));

        // O cliente bloqueado recebe 429 com qualquer e-mail.
        assertEquals(HttpStatus.TOO_MANY_REQUESTS, loginComSenhaErrada("c@email.com", "203.0.113.5, 10.0.0.7"));

        // Outro cliente, pelo mesmo proxy, não é afetado.
        assertEquals(HttpStatus.UNAUTHORIZED, loginComSenhaErrada("c@email.com", "203.0.113.9, 10.0.0.7"));

        // Forjar um IP no começo do cabeçalho não livra o cliente bloqueado:
        // vale o último endereço que não é de um proxy de confiança.
        assertEquals(HttpStatus.TOO_MANY_REQUESTS, loginComSenhaErrada("c@email.com", "198.51.100.1, 203.0.113.5, 10.0.0.7"));
    }
}
