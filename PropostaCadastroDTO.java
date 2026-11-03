package br.com.novaroupa.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/** Dados enviados pelo comprador para registrar interesse em um lote. */
public record PropostaCadastroDTO(
        @NotNull Long loteId,
        @NotBlank(message = "mensagem não pode estar vazia") @Size(max = 2000) String mensagem,
        @Size(max = 30) String telefoneContato) {
}
