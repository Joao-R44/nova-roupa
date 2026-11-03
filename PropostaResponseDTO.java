package br.com.novaroupa.dto;

import br.com.novaroupa.model.PropostaNegociacao;
import br.com.novaroupa.model.StatusPropostaNegociacao;
import java.time.LocalDateTime;

/** Representação segura de uma proposta para consumo da API. */
public record PropostaResponseDTO(Long id, Long loteId, String comprador, String vendedor,
                                  String mensagem, String telefoneContato,
                                  StatusPropostaNegociacao status,
                                  LocalDateTime dataCriacao, LocalDateTime dataAtualizacao) {
    public static PropostaResponseDTO de(PropostaNegociacao p) {
        return new PropostaResponseDTO(p.getId(), p.getLote().getId(),
                p.getComprador().getUsername(), p.getLote().getVendedor().getUsername(),
                p.getMensagem(), p.getTelefoneContato(), p.getStatus(),
                p.getDataCriacao(), p.getDataAtualizacao());
    }
}
