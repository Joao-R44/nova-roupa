package br.com.novaroupa.security;

import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LoginRateLimiterTests {

    @Test
    void allowsConfiguredAttemptsThenBlocksClient() {
        LoginRateLimiter limiter = new LoginRateLimiter(
                2, Duration.ofMinutes(1), fixedClock());

        assertTrue(limiter.tryAcquire("192.0.2.10").allowed());
        assertTrue(limiter.tryAcquire("192.0.2.10").allowed());
        LoginRateLimiter.Decision blocked = limiter.tryAcquire("192.0.2.10");

        assertFalse(blocked.allowed());
        assertTrue(blocked.retryAfterSeconds() > 0);
    }

    @Test
    void tracksClientsIndependently() {
        LoginRateLimiter limiter = new LoginRateLimiter(
                1, Duration.ofMinutes(1), fixedClock());

        assertTrue(limiter.tryAcquire("192.0.2.10").allowed());
        assertFalse(limiter.tryAcquire("192.0.2.10").allowed());
        assertTrue(limiter.tryAcquire("192.0.2.11").allowed());
    }

    private Clock fixedClock() {
        return Clock.fixed(Instant.parse("2026-11-03T13:00:00Z"), ZoneOffset.UTC);
    }
}
