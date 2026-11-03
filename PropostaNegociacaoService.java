package br.com.novaroupa.service;

import br.com.novaroupa.dto.PropostaCadastroDTO;
import br.com.novaroupa.dto.PropostaResponseDTO;
import br.com.novaroupa.dto.PropostaStatusDTO;
import br.com.novaroupa.model.LoteRetalho;
import br.com.novaroupa.model.PropostaNegociacao;
import br.com.novaroupa.model.StatusLote;
import br.com.novaroupa.model.StatusPropostaNegociacao;
import br.com.novaroupa.model.Usuario;
import br.com.novaroupa.repository.LoteRetalhoRepository;
import br.com.novaroupa.repository.PropostaNegociacaoRepository;
import br.com.novaroupa.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;

/** Orquestra o envio, consulta e decisão sobre propostas de negociação. */
@Service
public class PropostaNegociacaoService {
    private final PropostaNegociacaoRepository propostaRepository;
    private final LoteRetalhoRepository loteRepository;
    private final UsuarioRepository usuarioRepository;

    public PropostaNegociacaoService(PropostaNegociacaoRepository propostaRepository,
                                     LoteRetalhoRepository loteRepository,
                                     UsuarioRepository usuarioRepository) {
        this.propostaRepository = propostaRepository;
        this.loteRepository = loteRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public PropostaResponseDTO criar(PropostaCadastroDTO dados, String username) {
        Usuario comprador = usuario(username);
        LoteRetalho lote = loteRepository.findById(dados.loteId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lote não encontrado"));
        if (lote.getStatus() != StatusLote.DISPONIVEL) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Lote não está disponível");
        }
        if (Objects.equals(lote.getVendedor().getId(), comprador.getId())
                && lote.getVendedor().getUsername().equals(comprador.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Vendedor não pode propor ao próprio lote");
        }
        PropostaNegociacao proposta = propostaRepository.save(new PropostaNegociacao(
                lote, comprador, dados.mensagem(), dados.telefoneContato()));
        return PropostaResponseDTO.de(proposta);
    }

    @Transactional(readOnly = true)
    public List<PropostaResponseDTO> minhas(String username) {
        Usuario comprador = usuario(username);
        return propostaRepository.findByComprador(comprador).stream().map(PropostaResponseDTO::de).toList();
    }

    @Transactional(readOnly = true)
    public List<PropostaResponseDTO> recebidas(String username) {
        Usuario vendedor = usuario(username);
        return propostaRepository.findByLoteVendedor(vendedor).stream().map(PropostaResponseDTO::de).toList();
    }

    @Transactional
    public PropostaResponseDTO atualizarStatus(Long id, PropostaStatusDTO dados, String username) {
        PropostaNegociacao proposta = propostaRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Proposta não encontrada"));
        if (!proposta.getLote().getVendedor().getUsername().equals(username)) {
            throw new AccessDeniedException("Somente o vendedor do lote pode decidir a proposta");
        }
        if (dados.status() != StatusPropostaNegociacao.ACEITA
                && dados.status() != StatusPropostaNegociacao.RECUSADA) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status deve ser ACEITA ou RECUSADA");
        }
        if (proposta.getStatus() != StatusPropostaNegociacao.PENDENTE) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Proposta já foi decidida");
        }
        LoteRetalho lote = proposta.getLote();
        if (dados.status() == StatusPropostaNegociacao.ACEITA) {
            if (lote.getStatus() != StatusLote.DISPONIVEL) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Lote não está disponível para venda");
            }
            // Aceite representa venda imediata: o lote não pode mais ser oferecido.
            lote.setStatus(StatusLote.FINALIZADO);
            loteRepository.save(lote);
        }
        proposta.setStatus(dados.status());
        return PropostaResponseDTO.de(propostaRepository.save(proposta));
    }

    private Usuario usuario(String username) {
        return usuarioRepository.findByUsername(username).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Perfil local não encontrado"));
    }
}
