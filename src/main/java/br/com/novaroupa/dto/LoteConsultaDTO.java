package br.com.novaroupa.dto;

import br.com.novaroupa.model.LoteRetalho;
import br.com.novaroupa.model.StatusLote;
import br.com.novaroupa.model.TipoTecido;

import java.time.LocalDateTime;

/** Dados públicos de um lote retornados na consulta do catálogo. */
public record LoteConsultaDTO(
        Long id,
        TipoTecido tipoTecido,
        Double pesoKg,
        Double metragemAproximada,
        String corPredominante,
        String descricao,
        String fotosUrls,
        String localizacao,
        StatusLote status,
        LocalDateTime dataPublicacao) {

    public static LoteConsultaDTO de(LoteRetalho lote) {
        return new LoteConsultaDTO(
                lote.getId(),
                lote.getTipoTecido(),
                lote.getPesoKg(),
                lote.getMetragemAproximada(),
                lote.getCorPredominante(),
                lote.getDescricao(),
                lote.getFotosUrls(),
                lote.getLocalizacao(),
                lote.getStatus(),
                lote.getDataPublicacao());
    }
}
