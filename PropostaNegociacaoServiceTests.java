package br.com.novaroupa.service;

import br.com.novaroupa.dto.PropostaCadastroDTO;
import br.com.novaroupa.dto.PropostaStatusDTO;
import br.com.novaroupa.model.LoteRetalho;
import br.com.novaroupa.model.PropostaNegociacao;
import br.com.novaroupa.model.StatusLote;
import br.com.novaroupa.model.StatusPropostaNegociacao;
import br.com.novaroupa.model.TipoPerfil;
import br.com.novaroupa.model.TipoTecido;
import br.com.novaroupa.model.Usuario;
import br.com.novaroupa.repository.LoteRetalhoRepository;
import br.com.novaroupa.repository.PropostaNegociacaoRepository;
import br.com.novaroupa.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PropostaNegociacaoServiceTests {
    @Mock PropostaNegociacaoRepository propostaRepository;
    @Mock LoteRetalhoRepository loteRepository;
    @Mock UsuarioRepository usuarioRepository;
    private PropostaNegociacaoService service;
    private Usuario vendedor;
    private Usuario comprador;
    private LoteRetalho lote;

    @BeforeEach
    void setUp() {
        service = new PropostaNegociacaoService(propostaRepository, loteRepository, usuarioRepository);
        vendedor = new Usuario("vendedor-api", TipoPerfil.VENDEDOR);
        comprador = new Usuario("comprador-api", TipoPerfil.COMPRADOR);
        lote = new LoteRetalho(vendedor, TipoTecido.ALGODAO, 10.0, "Azul", "Recife");
        lenient().when(usuarioRepository.findByUsername("comprador-api")).thenReturn(Optional.of(comprador));
        lenient().when(usuarioRepository.findByUsername("vendedor-api")).thenReturn(Optional.of(vendedor));
        lenient().when(propostaRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    void createsProposalForAvailableLot() {
        when(loteRepository.findById(1L)).thenReturn(Optional.of(lote));
        var response = service.criar(new PropostaCadastroDTO(1L, "Tenho interesse", "81999999999"), "comprador-api");
        assertThat(response.mensagem()).isEqualTo("Tenho interesse");
        assertThat(response.status()).isEqualTo(StatusPropostaNegociacao.PENDENTE);
    }

    @Test
    void rejectsProposalForUnavailableLot() {
        lote.setStatus(StatusLote.FINALIZADO);
        when(loteRepository.findById(1L)).thenReturn(Optional.of(lote));
        assertThatThrownBy(() -> service.criar(new PropostaCadastroDTO(1L, "Tenho interesse", null), "comprador-api"))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Lote não está disponível");
    }

    @Test
    void acceptanceFinishesLotAndMarksProposalAccepted() {
        PropostaNegociacao proposta = new PropostaNegociacao(lote, comprador, "Interesse", null);
        when(propostaRepository.findById(1L)).thenReturn(Optional.of(proposta));
        var response = service.atualizarStatus(1L, new PropostaStatusDTO(StatusPropostaNegociacao.ACEITA), "vendedor-api");
        assertThat(response.status()).isEqualTo(StatusPropostaNegociacao.ACEITA);
        assertThat(lote.getStatus()).isEqualTo(StatusLote.FINALIZADO);
        verify(loteRepository).save(lote);
    }

    @Test
    void refusalKeepsLotAvailable() {
        PropostaNegociacao proposta = new PropostaNegociacao(lote, comprador, "Interesse", null);
        when(propostaRepository.findById(1L)).thenReturn(Optional.of(proposta));
        var response = service.atualizarStatus(1L, new PropostaStatusDTO(StatusPropostaNegociacao.RECUSADA), "vendedor-api");
        assertThat(response.status()).isEqualTo(StatusPropostaNegociacao.RECUSADA);
        assertThat(lote.getStatus()).isEqualTo(StatusLote.DISPONIVEL);
    }
}
