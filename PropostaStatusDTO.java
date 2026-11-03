package br.com.novaroupa.dto;

import br.com.novaroupa.model.StatusPropostaNegociacao;
import jakarta.validation.constraints.NotNull;

/** Status solicitado pelo vendedor para uma proposta pendente. */
public record PropostaStatusDTO(@NotNull StatusPropostaNegociacao status) {
}
