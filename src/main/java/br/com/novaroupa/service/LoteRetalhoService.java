package br.com.novaroupa.service;

import br.com.novaroupa.dto.LoteCadastroDTO;
import br.com.novaroupa.dto.LoteResponseDTO;
import br.com.novaroupa.model.LoteRetalho;
import br.com.novaroupa.model.Usuario;
import br.com.novaroupa.repository.LoteRetalhoRepository;
import br.com.novaroupa.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Regras de negócio para publicação de lotes de retalhos. */
@Service
public class LoteRetalhoService {

    private final LoteRetalhoRepository loteRetalhoRepository;
    private final UsuarioRepository usuarioRepository;

    public LoteRetalhoService(LoteRetalhoRepository loteRetalhoRepository,
                              UsuarioRepository usuarioRepository) {
        this.loteRetalhoRepository = loteRetalhoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    /** Salva o lote usando como vendedor o usuário identificado pelo token autenticado. */
    @Transactional
    public LoteResponseDTO cadastrarLote(LoteCadastroDTO dados, String usernameAutenticado) {
        Usuario vendedor = usuarioRepository.findByUsername(usernameAutenticado)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Perfil local não encontrado para o usuário autenticado"));

        LoteRetalho lote = new LoteRetalho(vendedor, dados.tipoTecido(), dados.pesoKg(),
                dados.corPredominante(), dados.localizacao());
        lote.setMetragemAproximada(dados.metragemAproximada());
        lote.setDescricao(dados.descricao());
        lote.setFotosUrls(dados.fotosUrls());

        LoteRetalho salvo = loteRetalhoRepository.save(lote);
        return new LoteResponseDTO(salvo.getId(), vendedor.getUsername(), salvo.getTipoTecido(),
                salvo.getPesoKg(), salvo.getMetragemAproximada(), salvo.getCorPredominante(),
                salvo.getDescricao(), salvo.getFotosUrls(), salvo.getLocalizacao(), salvo.getStatus(),
                salvo.getDataPublicacao());
    }
}
