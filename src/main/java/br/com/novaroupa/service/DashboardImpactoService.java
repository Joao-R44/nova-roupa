package br.com.novaroupa.service;

import br.com.novaroupa.dto.DashboardImpactoDTO;
import br.com.novaroupa.model.StatusLote;
import br.com.novaroupa.model.StatusPropostaNegociacao;
import br.com.novaroupa.repository.LoteRetalhoRepository;
import br.com.novaroupa.repository.PropostaNegociacaoRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/** Consolida indicadores de reuso a partir dos lotes e negociações persistidos. */
@Service
public class DashboardImpactoService {

    private static final double FATOR_REDUCAO_CARBONO_KG_POR_KG = 1.5;
    private static final Set<StatusLote> STATUS_REAPROVEITADO = Set.of(StatusLote.DOADO, StatusLote.FINALIZADO);
    private static final Set<StatusPropostaNegociacao> STATUS_NEGOCIACAO =
            Set.of(StatusPropostaNegociacao.ACEITA, StatusPropostaNegociacao.CONCLUIDA);
    private static final Set<StatusPropostaNegociacao> STATUS_COMPRADOR_ATIVO =
            Set.of(StatusPropostaNegociacao.PENDENTE, StatusPropostaNegociacao.ACEITA,
                    StatusPropostaNegociacao.CONCLUIDA);

    private final LoteRetalhoRepository loteRepository;
    private final PropostaNegociacaoRepository propostaRepository;

    public DashboardImpactoService(LoteRetalhoRepository loteRepository,
                                   PropostaNegociacaoRepository propostaRepository) {
        this.loteRepository = loteRepository;
        this.propostaRepository = propostaRepository;
    }

    @Transactional(readOnly = true)
    public DashboardImpactoDTO obterImpacto() {
        double quilos = loteRepository.sumPesoKgByStatusIn(STATUS_REAPROVEITADO);
        List<DashboardImpactoDTO.CategoriaImpactoDTO> categorias = loteRepository
                .findCategoriasByStatusIn(STATUS_REAPROVEITADO, PageRequest.of(0, 5))
                .stream()
                .map(item -> new DashboardImpactoDTO.CategoriaImpactoDTO(
                        item.getCategoria().name(), item.getQuilos()))
                .toList();

        return new DashboardImpactoDTO(
                quilos,
                propostaRepository.countDistinctLotesByStatusIn(STATUS_NEGOCIACAO),
                loteRepository.countDistinctVendedoresByStatusIn(STATUS_REAPROVEITADO),
                propostaRepository.countDistinctCompradoresByStatusIn(STATUS_COMPRADOR_ATIVO),
                quilos * FATOR_REDUCAO_CARBONO_KG_POR_KG,
                categorias);
    }
}
