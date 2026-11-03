package br.com.novaroupa.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/** Dados necessários para registrar uma avaliação de parceiro. */
public record AvaliacaoDTO(
        @NotNull(message = "propostaId é obrigatório") Long propostaId,
        @NotNull(message = "nota é obrigatória")
        @Min(value = 1, message = "nota deve ser no mínimo 1")
        @Max(value = 5, message = "nota deve ser no máximo 5") Integer nota,
        @Size(max = 500, message = "comentário deve ter no máximo 500 caracteres") String comentario) {
}
