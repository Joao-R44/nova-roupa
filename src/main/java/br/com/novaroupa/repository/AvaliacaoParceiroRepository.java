package br.com.novaroupa.repository;

import br.com.novaroupa.model.AvaliacaoParceiro;
import br.com.novaroupa.model.PropostaNegociacao;
import br.com.novaroupa.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/** Acesso às avaliações de parceiros persistidas. */
public interface AvaliacaoParceiroRepository extends JpaRepository<AvaliacaoParceiro, Long> {
    Optional<AvaliacaoParceiro> findByPropostaAndAvaliador(PropostaNegociacao proposta, Usuario avaliador);
}
