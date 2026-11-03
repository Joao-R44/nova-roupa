package br.com.novaroupa.service;

import br.com.novaroupa.dto.LoteConsultaDTO;
import br.com.novaroupa.model.TipoTecido;
import br.com.novaroupa.repository.LoteRetalhoRepository;
import br.com.novaroupa.specification.LoteSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Regras de consulta pública do catálogo de lotes. */
@Service
@Transactional(readOnly = true)
public class LoteConsultaService {

    private final LoteRetalhoRepository loteRepository;

    public LoteConsultaService(LoteRetalhoRepository loteRepository) {
        this.loteRepository = loteRepository;
    }

    public Page<LoteConsultaDTO> buscar(TipoTecido tipoTecido, String cor,
                                        Double pesoMin, Double pesoMax,
                                        String localizacao, int page, int size) {
        Pageable pageable = PageRequest.of(page, size,
                Sort.by(Sort.Direction.DESC, "dataPublicacao"));
        return loteRepository.findAll(
                        LoteSpecification.comFiltros(tipoTecido, cor, pesoMin, pesoMax, localizacao),
                        pageable)
                .map(LoteConsultaDTO::de);
    }
}
