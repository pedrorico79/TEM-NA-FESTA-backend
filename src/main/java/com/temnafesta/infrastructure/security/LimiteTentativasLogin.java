package com.temnafesta.infrastructure.security;

import com.temnafesta.application.exception.TentativasLoginExcedidasException;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class LimiteTentativasLogin {
    private static final Duration JANELA = Duration.ofMinutes(15);
    private static final Duration BLOQUEIO = Duration.ofMinutes(15);
    private static final int MAX_POR_CONTA_E_IP = 5;
    private static final int MAX_POR_IP = 20;

    private final Map<String, Registro> registros = new ConcurrentHashMap<>();

    public synchronized void validarTentativa(String ip, String email) {
        Instant agora = Instant.now();
        limparExpirados(agora);
        validarRegistro(registros.get(chaveConta(ip, email)), agora);
        validarRegistro(registros.get(chaveIp(ip)), agora);
    }

    public synchronized void registrarFalha(String ip, String email) {
        Instant agora = Instant.now();
        registrar(chaveConta(ip, email), MAX_POR_CONTA_E_IP, agora);
        registrar(chaveIp(ip), MAX_POR_IP, agora);
    }

    public synchronized void registrarSucesso(String ip, String email) {
        registros.remove(chaveConta(ip, email));
    }

    private void registrar(String chave, int limite, Instant agora) {
        Registro registro = registros.compute(chave, (ignorada, existente) -> {
            if (existente == null || Duration.between(existente.inicioJanela, agora).compareTo(JANELA) >= 0) {
                return new Registro(agora);
            }
            return existente;
        });
        registro.falhas++;
        if (registro.falhas >= limite) {
            registro.bloqueadoAte = agora.plus(BLOQUEIO);
        }
    }

    private void validarRegistro(Registro registro, Instant agora) {
        if (registro == null) return;
        if (registro.bloqueadoAte != null && registro.bloqueadoAte.isAfter(agora)) {
            long segundos = Math.max(1, Duration.between(agora, registro.bloqueadoAte).toSeconds());
            throw new TentativasLoginExcedidasException(segundos);
        }
    }

    private void limparExpirados(Instant agora) {
        registros.entrySet().removeIf(entry -> {
            Registro registro = entry.getValue();
            Instant expiracao = registro.bloqueadoAte != null ? registro.bloqueadoAte : registro.inicioJanela.plus(JANELA);
            return !expiracao.isAfter(agora);
        });
    }

    private String chaveConta(String ip, String email) {
        return "conta:" + normalizarIp(ip) + ":" + normalizarEmail(email);
    }

    private String chaveIp(String ip) {
        return "ip:" + normalizarIp(ip);
    }

    private String normalizarIp(String ip) {
        return ip == null || ip.isBlank() ? "desconhecido" : ip.trim();
    }

    private String normalizarEmail(String email) {
        return email == null ? "" : email.trim().toLowerCase(Locale.ROOT);
    }

    private static final class Registro {
        private final Instant inicioJanela;
        private int falhas;
        private Instant bloqueadoAte;

        private Registro(Instant inicioJanela) {
            this.inicioJanela = inicioJanela;
        }
    }
}
