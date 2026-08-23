package com.temnafesta.infrastructure.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.expiration-ms:86400000}") // 24 horas
    private long jwtExpirationMs;

    @Value("${app.jwt.expiration-remember-ms:2592000000}") // 30 dias
    private long jwtExpirationRememberMeMs;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    public String gerarToken(String email, String perfil, boolean jwtValidityRememberMe) {
        long expirationMs = jwtValidityRememberMe ? jwtExpirationRememberMeMs : jwtExpirationMs;
        return gerarToken(email, perfil, expirationMs);
    }

    private String gerarToken(String email, String perfil, long expirationMs) {
        Date agora = new Date();
        Date dataExpiracao = new Date(agora.getTime() + expirationMs);

        return Jwts.builder()
                .subject(email)
                .claim("role", perfil)
                .issuedAt(agora)
                .expiration(dataExpiracao)
                .signWith(getSigningKey())
                .compact();
    }

    public long getExpiracaoMs(boolean jwtValidityRememberMe) {
        return jwtValidityRememberMe ? jwtExpirationRememberMeMs : jwtExpirationMs;
    }

    public String getEmailDoToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.getSubject();
    }

    public boolean validarToken(String token) {
        try {
            Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}