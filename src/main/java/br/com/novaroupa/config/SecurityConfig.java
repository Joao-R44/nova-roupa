package br.com.novaroupa.config;

import br.com.novaroupa.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/** Regras comuns de acesso HTTP. As estratégias de autenticação variam por profile. */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

 @Bean
 SecurityFilterChain securityFilterChain(HttpSecurity http,
 JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
 return http
 .csrf(csrf -> csrf.disable())
 .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
 .authorizeHttpRequests(auth -> auth
 .requestMatchers("/api/auth/login", "/api/auth/**", "/actuator/health", "/v3/api-docs/**", "/swagger-ui/**")
 .permitAll()
 .requestMatchers(HttpMethod.GET, "/api/lotes", "/api/dashboard/impacto").permitAll()
 .anyRequest().authenticated())
 .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
 .build();
 }

 @Bean
 AuthenticationManager authenticationManager(org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration configuration)
 throws Exception {
 return configuration.getAuthenticationManager();
 }
}
