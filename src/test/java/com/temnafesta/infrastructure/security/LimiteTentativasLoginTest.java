package com.temnafesta.infrastructure.security;

import com.temnafesta.application.exception.TentativasLoginExcedidasException;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LimiteTentativasLoginTest {

    private static final Duration QUINZE_MINUTOS = Duration.ofMinutes(15);

    private final RelogioAjustavel relogio = new RelogioAjustavel();

    private LimiteTentativasLogin limite(int maxPorConta, int maxPorIp) {
        return new LimiteTentativasLogin(maxPorConta, maxPorIp, QUINZE_MINUTOS, QUINZE_MINUTOS, relogio);
    }

    private static void falhar(LimiteTentativasLogin limite, String ip, String email, int vezes) {
        for (int i = 0; i < vezes; i++) {
            limite.registrarFalha(ip, email);
        }
    }

    @Test
    void bloqueiaAContaDepoisDoLimitePorContaSemAfetarOutroEmail() {
        LimiteTentativasLogin limite = limite(5, 20);
        falhar(limite, "203.0.113.5", "ana@email.com", 5);

        assertThrows(TentativasLoginExcedidasException.class,
                () -> limite.validarTentativa("203.0.113.5", "ANA@email.com "));
        assertDoesNotThrow(() -> limite.validarTentativa("203.0.113.5", "bia@email.com"));
    }

    @Test
    void bloqueiaOIpDepoisDoLimitePorIpParaQualquerEmailSemAfetarOutroIp() {
        LimiteTentativasLogin limite = limite(5, 20);
        for (int i = 0; i < 20; i++) {
            limite.registrarFalha("203.0.113.5", "usuario" + i + "@email.com");
        }

        assertThrows(TentativasLoginExcedidasException.class,
                () -> limite.validarTentativa("203.0.113.5", "outro@email.com"));
        assertDoesNotThrow(() -> limite.validarTentativa("203.0.113.9", "outro@email.com"));
    }

    @Test
    void limitePorIpZeroDesligaSoARegraPorIp() {
        LimiteTentativasLogin limite = limite(5, 0);
        for (int i = 0; i < 500; i++) {
            limite.registrarFalha("203.0.113.5", "usuario" + i + "@email.com");
        }
        assertDoesNotThrow(() -> limite.validarTentativa("203.0.113.5", "outro@email.com"));

        falhar(limite, "203.0.113.5", "ana@email.com", 5);
        assertThrows(TentativasLoginExcedidasException.class,
                () -> limite.validarTentativa("203.0.113.5", "ana@email.com"));
    }

    @Test
    void limitePorContaZeroDesligaSoARegraPorConta() {
        LimiteTentativasLogin limite = limite(0, 20);
        falhar(limite, "203.0.113.5", "ana@email.com", 19);
        assertDoesNotThrow(() -> limite.validarTentativa("203.0.113.5", "ana@email.com"));

        falhar(limite, "203.0.113.5", "ana@email.com", 1);
        assertThrows(TentativasLoginExcedidasException.class,
                () -> limite.validarTentativa("203.0.113.5", "ana@email.com"));
    }

    @Test
    void bloqueioExpiraDepoisDoTempoConfigurado() {
        LimiteTentativasLogin limite = limite(5, 20);
        falhar(limite, "203.0.113.5", "ana@email.com", 5);

        relogio.avancar(Duration.ofMinutes(14));
        assertThrows(TentativasLoginExcedidasException.class,
                () -> limite.validarTentativa("203.0.113.5", "ana@email.com"));

        relogio.avancar(Duration.ofMinutes(1));
        assertDoesNotThrow(() -> limite.validarTentativa("203.0.113.5", "ana@email.com"));
    }

    @Test
    void falhasForaDaJanelaNaoSeSomam() {
        LimiteTentativasLogin limite = limite(5, 20);
        falhar(limite, "203.0.113.5", "ana@email.com", 4);
        relogio.avancar(Duration.ofMinutes(15));
        falhar(limite, "203.0.113.5", "ana@email.com", 4);

        assertDoesNotThrow(() -> limite.validarTentativa("203.0.113.5", "ana@email.com"));
    }

    @Test
    void sucessoZeraAsFalhasDaConta() {
        LimiteTentativasLogin limite = limite(5, 20);
        falhar(limite, "203.0.113.5", "ana@email.com", 4);
        limite.registrarSucesso("203.0.113.5", "ana@email.com");
        falhar(limite, "203.0.113.5", "ana@email.com", 4);

        assertDoesNotThrow(() -> limite.validarTentativa("203.0.113.5", "ana@email.com"));
    }

    @Test
    void janelaOuBloqueioZeroSaoRecusados() {
        assertThrows(IllegalArgumentException.class,
                () -> new LimiteTentativasLogin(5, 20, Duration.ZERO, QUINZE_MINUTOS, relogio));
        assertThrows(IllegalArgumentException.class,
                () -> new LimiteTentativasLogin(5, 20, QUINZE_MINUTOS, Duration.ZERO, relogio));
    }

    private static final class RelogioAjustavel extends Clock {
        private Instant agora = Instant.parse("2026-11-11T12:00:00Z");

        void avancar(Duration duracao) {
            agora = agora.plus(duracao);
        }

        @Override
        public ZoneId getZone() {
            return ZoneId.of("UTC");
        }

        @Override
        public Clock withZone(ZoneId zone) {
            return this;
        }

        @Override
        public Instant instant() {
            return agora;
        }
    }
}
