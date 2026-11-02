package br.com.novaroupa.dto;

import java.util.List;

/** Resposta padronizada após autenticação bem-sucedida. */
public record LoginResponseDTO(
        String token,
        String type,
        String username,
        List<String> roles,
        long expiresIn
) {
}
