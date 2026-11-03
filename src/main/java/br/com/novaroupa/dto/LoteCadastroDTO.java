package br.com.novaroupa.dto;

import br.com.novaroupa.model.TipoTecido;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

/** Dados aceitos para publicar um lote; o vendedor é obtido da autenticação, nunca do corpo. */
public record LoteCadastroDTO(
        @NotNull TipoTecido tipoTecido,
        @NotNull @Positive Double pesoKg,
        @Positive Double metragemAproximada,
        @NotBlank @Size(max = 60) String corPredominante,
        @Size(max = 1000) String descricao,
        @Size(max = 2000) String fotosUrls,
        @NotBlank @Size(max = 120) String localizacao
) {
}
