package br.com.novaroupa.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Date;
import java.util.List;

/** Centraliza a criação e validação dos tokens de acesso da API. */
@Component
public class JwtTokenProvider {

    private final SecretKey key;
    private final long expirationMs;

    public JwtTokenProvider(@Value("${app.jwt.secret:}") String configuredSecret,
                            @Value("${app.jwt.expiration-ms:3600000}") long expirationMs,
                            Environment environment) {
        this.key = createKey(configuredSecret, environment);
        this.expirationMs = expirationMs;
    }

    private SecretKey createKey(String configuredSecret, Environment environment) {
        if (configuredSecret == null || configuredSecret.isBlank()) {
            if (!environment.acceptsProfiles(Profiles.of("dev", "test"))) {
                throw new IllegalStateException("JWT_SECRET deve ser configurado fora dos profiles dev/test");
            }
            // Para desenvolvimento local/testes: os tokens deixam de ser válidos após reiniciar a aplicação.
            byte[] bytes = new byte[32];
            new SecureRandom().nextBytes(bytes);
            return Keys.hmacShaKeyFor(bytes);
        }
        byte[] bytes = configuredSecret.getBytes(StandardCharsets.UTF_8);
        if (bytes.length < 32) {
            throw new IllegalArgumentException("JWT_SECRET deve ter pelo menos 32 bytes para HS256");
        }
        return Keys.hmacShaKeyFor(bytes);
    }

    public String generateToken(Authentication authentication) {
        Date now = new Date();
        List<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        return Jwts.builder()
                .subject(authentication.getName())
                .claim("roles", roles)
                .issuedAt(now)
                .expiration(new Date(now.getTime() + expirationMs))
                .signWith(key)
                .compact();
    }

    public Claims parseClaims(String token) {
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
    }

    public boolean isValid(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (RuntimeException exception) {
            return false;
        }
    }

    public long getExpirationMs() {
        return expirationMs;
    }
}
