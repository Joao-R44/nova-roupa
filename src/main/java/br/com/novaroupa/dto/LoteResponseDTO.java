package br.com.novaroupa.dto;

import br.com.novaroupa.model.StatusLote;
import br.com.novaroupa.model.TipoTecido;

import java.time.LocalDateTime;

/** Dados públicos retornados após a criação de um lote. */
public record LoteResponseDTO(
        Long id,
        String vendedor,
        TipoTecido tipoTecido,
        Double pesoKg,
        Double metragemAproximada,
        String corPredominante,
        String descricao,
        String fotosUrls,
        String localizacao,
        StatusLote status,
        LocalDateTime dataPublicacao
) {
}
