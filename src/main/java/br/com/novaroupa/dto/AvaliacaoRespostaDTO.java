package br.com.novaroupa.dto;

import br.com.novaroupa.model.AvaliacaoParceiro;

import java.time.LocalDateTime;

/** Representação pública da avaliação registrada. */
public record AvaliacaoRespostaDTO(Long id, Long propostaId, Long avaliadorId, Long avaliadoId,
                                   Integer nota, String comentario, LocalDateTime dataCriacao) {
    public static AvaliacaoRespostaDTO from(AvaliacaoParceiro avaliacao) {
        return new AvaliacaoRespostaDTO(avaliacao.getId(), avaliacao.getProposta().getId(),
                avaliacao.getAvaliador().getId(), avaliacao.getAvaliado().getId(), avaliacao.getNota(),
                avaliacao.getComentario(), avaliacao.getDataCriacao());
    }
}
