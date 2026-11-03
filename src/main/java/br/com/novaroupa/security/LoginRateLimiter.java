package br.com.novaroupa.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.Duration;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.ConcurrentHashMap;

/** Limita tentativas de login por origem para reduzir ataques de força bruta. */
@Component
public class LoginRateLimiter {
    private final int maxAttempts;
    private final Duration window;
    private final Clock clock;
    private final ConcurrentHashMap<String, Deque<Long>> attemptsByClient = new ConcurrentHashMap<>();

    @Autowired
    public LoginRateLimiter(
            @Value("${app.security.login-rate-limit.max-attempts:5}") int maxAttempts,
            @Value("${app.security.login-rate-limit.window-ms:60000}") long windowMs) {
        this(maxAttempts, Duration.ofMillis(windowMs), Clock.systemUTC());
    }

    LoginRateLimiter(int maxAttempts, Duration window, Clock clock) {
        if (maxAttempts < 1) {
            throw new IllegalArgumentException("maxAttempts deve ser maior que zero");
        }
        if (window.isZero() || window.isNegative()) {
            throw new IllegalArgumentException("window deve ser maior que zero");
        }
        this.maxAttempts = maxAttempts;
        this.window = window;
        this.clock = clock;
    }

    /** Registra uma tentativa e informa se ela pode prosseguir. */
    public Decision tryAcquire(String clientKey) {
        String key = clientKey == null || clientKey.isBlank() ? "unknown" : clientKey;
        long now = clock.millis();
        long cutoff = now - window.toMillis();
        Deque<Long> attempts = attemptsByClient.computeIfAbsent(key, ignored -> new ArrayDeque<>());

        synchronized (attempts) {
            while (!attempts.isEmpty() && attempts.peekFirst() <= cutoff) {
                attempts.removeFirst();
            }
            if (attempts.size() >= maxAttempts) {
                long retryAfterMillis = window.toMillis() - (now - attempts.peekFirst());
                long retryAfterSeconds = Math.max(1, (retryAfterMillis + 999) / 1000);
                return new Decision(false, retryAfterSeconds);
            }
            attempts.addLast(now);
            return new Decision(true, 0);
        }
    }

    public record Decision(boolean allowed, long retryAfterSeconds) {
    }
}
