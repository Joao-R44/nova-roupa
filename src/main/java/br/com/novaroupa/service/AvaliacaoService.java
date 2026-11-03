package br.com.novaroupa.service;

import br.com.novaroupa.dto.AvaliacaoDTO;
import br.com.novaroupa.model.AvaliacaoParceiro;
import br.com.novaroupa.model.PropostaNegociacao;
import br.com.novaroupa.model.StatusPropostaNegociacao;
import br.com.novaroupa.model.Usuario;
import br.com.novaroupa.repository.AvaliacaoParceiroRepository;
import br.com.novaroupa.repository.PropostaNegociacaoRepository;
import br.com.novaroupa.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/** Registra avaliações somente entre participantes de negociações concluídas. */
@Service
public class AvaliacaoService {

    private final AvaliacaoParceiroRepository avaliacaoRepository;
    private final PropostaNegociacaoRepository propostaRepository;
    private final UsuarioRepository usuarioRepository;

    public AvaliacaoService(AvaliacaoParceiroRepository avaliacaoRepository,
                            PropostaNegociacaoRepository propostaRepository,
                            UsuarioRepository usuarioRepository) {
        this.avaliacaoRepository = avaliacaoRepository;
        this.propostaRepository = propostaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public AvaliacaoParceiro registrar(AvaliacaoDTO dados, String username) {
        Usuario avaliador = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN,
                        "Usuário autenticado não possui perfil local"));
        PropostaNegociacao proposta = propostaRepository.findById(dados.propostaId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Proposta não encontrada"));

        if (proposta.getStatus() != StatusPropostaNegociacao.CONCLUIDA) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "A avaliação só pode ser registrada após a conclusão da negociação");
        }

        Usuario avaliado = identificarAvaliado(proposta, avaliador);
        if (avaliacaoRepository.findByPropostaAndAvaliador(proposta, avaliador).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "A avaliação desta negociação já foi registrada");
        }

        AvaliacaoParceiro avaliacao = avaliacaoRepository.save(
                new AvaliacaoParceiro(proposta, avaliador, avaliado, dados.nota(), dados.comentario()));
        recalcularReputacao(avaliado);
        return avaliacao;
    }

    private Usuario identificarAvaliado(PropostaNegociacao proposta, Usuario avaliador) {
        if (proposta.getComprador().getId().equals(avaliador.getId())) {
            return proposta.getLote().getVendedor();
        }
        if (proposta.getLote().getVendedor().getId().equals(avaliador.getId())) {
            return proposta.getComprador();
        }
        throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                "Somente participantes da negociação podem avaliar");
    }

    private void recalcularReputacao(Usuario avaliado) {
        List<AvaliacaoParceiro> avaliacoes = avaliacaoRepository.findByAvaliado(avaliado);
        double media = avaliacoes.stream().mapToInt(AvaliacaoParceiro::getNota).average().orElse(0.0);
        avaliado.setReputacaoMedia(Math.round(media * 100.0) / 100.0);
        avaliado.setTotalAvaliacoes(avaliacoes.size());
        usuarioRepository.save(avaliado);
    }
}
