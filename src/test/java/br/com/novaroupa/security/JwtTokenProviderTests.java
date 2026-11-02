package br.com.novaroupa.security;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.springframework.mock.env.MockEnvironment;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JwtTokenProviderTests {

    @Test
    void generatedTokenContainsSubjectAndRolesAndIsValid() {
        long expirationMs = 60_000L;
        JwtTokenProvider provider = new JwtTokenProvider("", expirationMs, devEnvironment());

        String token = provider.generateToken(authentication());
        Claims claims = provider.parseClaims(token);

        assertEquals("vendedor", claims.getSubject());
        assertEquals(List.of("ROLE_VENDEDOR"), claims.get("roles"));
        assertEquals(expirationMs, provider.getExpirationMs());
        assertTrue(provider.isValid(token));
    }

    @Test
    void tokenSignedWithAnotherKeyIsRejected() {
        JwtTokenProvider issuer = new JwtTokenProvider("", 60_000L, devEnvironment());
        JwtTokenProvider validator = new JwtTokenProvider("", 60_000L, devEnvironment());
        String token = issuer.generateToken(authentication());

        assertFalse(validator.isValid(token));
    }

    @Test
    void expiredTokenIsRejected() {
        JwtTokenProvider provider = new JwtTokenProvider("", -60_000L, devEnvironment());
        String token = provider.generateToken(authentication());

        assertFalse(provider.isValid(token));
    }

    @Test
    void secretIsRequiredAndMustBeStrongOutsideDevAndTest() {
        MockEnvironment production = new MockEnvironment();
        production.setActiveProfiles("prod");

        assertThrows(IllegalStateException.class,
                () -> new JwtTokenProvider("", 3_600_000L, production));
        assertThrows(IllegalArgumentException.class,
                () -> new JwtTokenProvider("short", 3_600_000L, production));
    }

    private Authentication authentication() {
        return new UsernamePasswordAuthenticationToken(
                "vendedor",
                "ignored",
                List.of(new SimpleGrantedAuthority("ROLE_VENDEDOR")));
    }

    private MockEnvironment devEnvironment() {
        MockEnvironment environment = new MockEnvironment();
        environment.setActiveProfiles("dev");
        return environment;
    }
}
