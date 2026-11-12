package com.temnafesta.infrastructure.security;

import com.temnafesta.application.exception.TentativasLoginExcedidasException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Limite de tentativas de login que falharam, por conta e por IP.
 *
 * Os limites vêm das propriedades app.login.* (variáveis LOGIN_*). Um limite
 * igual a zero ou negativo desliga aquela regra. O IP recebido é o do cliente:
 * com server.forward-headers-strategy=native, o Tomcat o lê do X-Forwarded-For
 * quando a conexão vem de um proxy de confiança.
 *
 * Os contadores ficam na memória de cada instância da aplicação.
 */
@Component
public class LimiteTentativasLogin {
    private final int maxPorContaEIp;
    private final int maxPorIp;
    private final Duration janela;
    private final Duration bloqueio;
    private final Clock relogio;

    private final Map<String, Registro> registros = new ConcurrentHashMap<>();

    @Autowired
    public LimiteTentativasLogin(@Value("${app.login.max-por-conta:5}") int maxPorContaEIp,
                                 @Value("${app.login.max-por-ip:20}") int maxPorIp,
                                 @Value("${app.login.janela-minutos:15}") long janelaMinutos,
                                 @Value("${app.login.bloqueio-minutos:15}") long bloqueioMinutos) {
        this(maxPorContaEIp, maxPorIp, Duration.ofMinutes(janelaMinutos), Duration.ofMinutes(bloqueioMinutos),
                Clock.systemUTC());
    }

    LimiteTentativasLogin(int maxPorContaEIp, int maxPorIp, Duration janela, Duration bloqueio, Clock relogio) {
        if (janela.isZero() || janela.isNegative() || bloqueio.isZero() || bloqueio.isNegative()) {
            throw new IllegalArgumentException("app.login.janela-minutos e app.login.bloqueio-minutos devem ser maiores que zero.");
        }
        this.maxPorContaEIp = maxPorContaEIp;
        this.maxPorIp = maxPorIp;
        this.janela = janela;
        this.bloqueio = bloqueio;
        this.relogio = relogio;
    }

    public synchronized void validarTentativa(String ip, String email) {
        Instant agora = relogio.instant();
        limparExpirados(agora);
        validarRegistro(registros.get(chaveConta(ip, email)), agora);
        validarRegistro(registros.get(chaveIp(ip)), agora);
    }

    public synchronized void registrarFalha(String ip, String email) {
        Instant agora = relogio.instant();
        registrar(chaveConta(ip, email), maxPorContaEIp, agora);
        registrar(chaveIp(ip), maxPorIp, agora);
    }

    public synchronized void registrarSucesso(String ip, String email) {
        registros.remove(chaveConta(ip, email));
    }

    private void registrar(String chave, int limite, Instant agora) {
        if (limite <= 0) {
            return;
        }
        Registro registro = registros.compute(chave, (ignorada, existente) -> {
            if (existente == null || Duration.between(existente.inicioJanela, agora).compareTo(janela) >= 0) {
                return new Registro(agora);
            }
            return existente;
        });
        registro.falhas++;
        if (registro.falhas >= limite) {
            registro.bloqueadoAte = agora.plus(bloqueio);
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
            Instant expiracao = registro.bloqueadoAte != null ? registro.bloqueadoAte : registro.inicioJanela.plus(janela);
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
