package br.com.novaroupa.dto;

import jakarta.validation.constraints.NotBlank;

/** Credenciais recebidas no endpoint de login. */
public record LoginRequestDTO(
        @NotBlank String username,
        @NotBlank String password
) {
}
