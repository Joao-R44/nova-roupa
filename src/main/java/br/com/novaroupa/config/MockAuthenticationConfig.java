package br.com.novaroupa.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

/** Identidades descartáveis para desenvolvimento local; nunca são habilitadas no profile ldap. */
@Configuration
@Profile("dev")
public class MockAuthenticationConfig {

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    UserDetailsService mockUsers(PasswordEncoder passwordEncoder) {
        return new InMemoryUserDetailsManager(
                User.withUsername("admin").password(passwordEncoder.encode("admin-demo-123"))
                        .roles("ADMIN").build(),
                User.withUsername("vendedor").password(passwordEncoder.encode("vendedor-demo-123"))
                        .roles("VENDEDOR").build(),
                User.withUsername("comprador").password(passwordEncoder.encode("comprador-demo-123"))
                        .roles("COMPRADOR").build()
        );
    }
}
