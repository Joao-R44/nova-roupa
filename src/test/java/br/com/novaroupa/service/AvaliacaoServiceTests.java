package br.com.novaroupa.service;

import br.com.novaroupa.dto.AvaliacaoDTO;
import br.com.novaroupa.model.AvaliacaoParceiro;
import br.com.novaroupa.model.LoteRetalho;
import br.com.novaroupa.model.PropostaNegociacao;
import br.com.novaroupa.model.StatusPropostaNegociacao;
import br.com.novaroupa.model.Usuario;
import br.com.novaroupa.repository.AvaliacaoParceiroRepository;
import br.com.novaroupa.repository.PropostaNegociacaoRepository;
import br.com.novaroupa.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AvaliacaoServiceTests {

    private final AvaliacaoParceiroRepository avaliacaoRepository = mock(AvaliacaoParceiroRepository.class);
    private final PropostaNegociacaoRepository propostaRepository = mock(PropostaNegociacaoRepository.class);
    private final UsuarioRepository usuarioRepository = mock(UsuarioRepository.class);
    private final AvaliacaoService service = new AvaliacaoService(
            avaliacaoRepository, propostaRepository, usuarioRepository);

    private final Usuario comprador = mock(Usuario.class);
    private final Usuario vendedor = mock(Usuario.class);
    private final Usuario terceiro = mock(Usuario.class);
    private final LoteRetalho lote = mock(LoteRetalho.class);
    private final PropostaNegociacao proposta = mock(PropostaNegociacao.class);

    @BeforeEach
    void configurarParticipantes() {
        when(comprador.getId()).thenReturn(1L);
        when(vendedor.getId()).thenReturn(2L);
        when(terceiro.getId()).thenReturn(3L);
        when(proposta.getComprador()).thenReturn(comprador);
        when(proposta.getLote()).thenReturn(lote);
        when(lote.getVendedor()).thenReturn(vendedor);
        when(proposta.getStatus()).thenReturn(StatusPropostaNegociacao.CONCLUIDA);
        when(propostaRepository.findById(77L)).thenReturn(Optional.of(proposta));
        when(avaliacaoRepository.findByPropostaAndAvaliador(proposta, comprador)).thenReturn(Optional.empty());
        when(avaliacaoRepository.findByPropostaAndAvaliador(proposta, vendedor)).thenReturn(Optional.empty());
        when(avaliacaoRepository.save(any(AvaliacaoParceiro.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    void rejeitaUtilizadorSemPerfilLocal() {
        when(usuarioRepository.findByUsername("desconhecido")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.registrar(new AvaliacaoDTO(77L, 5, "Bom"), "desconhecido"))
                .isInstanceOf(ResponseStatusException.class)
                .extracting(error -> ((ResponseStatusException) error).getStatusCode())
                .isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    void devolveNotFoundQuandoPropostaNaoExiste() {
        when(usuarioRepository.findByUsername("comprador")).thenReturn(Optional.of(comprador));
        when(propostaRepository.findById(77L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.registrar(new AvaliacaoDTO(77L, 5, null), "comprador"))
                .isInstanceOf(ResponseStatusException.class)
                .extracting(error -> ((ResponseStatusException) error).getStatusCode())
                .isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void soPermiteAvaliarDepoisDaConclusaoDaNegociacao() {
        when(usuarioRepository.findByUsername("comprador")).thenReturn(Optional.of(comprador));
        when(proposta.getStatus()).thenReturn(StatusPropostaNegociacao.PENDENTE);

        assertThatThrownBy(() -> service.registrar(new AvaliacaoDTO(77L, 4, null), "comprador"))
                .isInstanceOf(ResponseStatusException.class)
                .extracting(error -> ((ResponseStatusException) error).getStatusCode())
                .isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    void bloqueiaAvaliacaoPorUtilizadorQueNaoParticipouNaNegociacao() {
        when(usuarioRepository.findByUsername("terceiro")).thenReturn(Optional.of(terceiro));

        assertThatThrownBy(() -> service.registrar(new AvaliacaoDTO(77L, 4, null), "terceiro"))
                .isInstanceOf(ResponseStatusException.class)
                .extracting(error -> ((ResponseStatusException) error).getStatusCode())
                .isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    void impedeUmaSegundaAvaliacaoDoMesmoParticipante() {
        when(usuarioRepository.findByUsername("comprador")).thenReturn(Optional.of(comprador));
        when(avaliacaoRepository.findByPropostaAndAvaliador(proposta, comprador))
                .thenReturn(Optional.of(mock(AvaliacaoParceiro.class)));

        assertThatThrownBy(() -> service.registrar(new AvaliacaoDTO(77L, 4, null), "comprador"))
                .isInstanceOf(ResponseStatusException.class)
                .extracting(error -> ((ResponseStatusException) error).getStatusCode())
                .isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    void compradorAvaliaVendedorEReputacaoMediaERecalculada() {
        when(usuarioRepository.findByUsername("comprador")).thenReturn(Optional.of(comprador));
        AvaliacaoParceiro anterior = new AvaliacaoParceiro(proposta, comprador, vendedor, 5, "Excelente");
        AvaliacaoParceiro atual = new AvaliacaoParceiro(proposta, comprador, vendedor, 4, "Bom");
        when(avaliacaoRepository.findByAvaliado(vendedor)).thenReturn(List.of(anterior, atual));

        AvaliacaoParceiro resultado = service.registrar(new AvaliacaoDTO(77L, 4, "Bom"), "comprador");

        assertThat(resultado.getAvaliado()).isSameAs(vendedor);
        assertThat(resultado.getAvaliador()).isSameAs(comprador);
        assertThat(resultado.getNota()).isEqualTo(4);
        verify(vendedor).setReputacaoMedia(4.5);
        verify(vendedor).setTotalAvaliacoes(2);
        verify(usuarioRepository).save(vendedor);
    }

    @Test
    void vendedorPodeAvaliarComprador() {
        when(usuarioRepository.findByUsername("vendedor")).thenReturn(Optional.of(vendedor));
        AvaliacaoParceiro avaliacao = new AvaliacaoParceiro(proposta, vendedor, comprador, 5, null);
        when(avaliacaoRepository.findByAvaliado(comprador)).thenReturn(List.of(avaliacao));

        AvaliacaoParceiro resultado = service.registrar(new AvaliacaoDTO(77L, 5, null), "vendedor");

        assertThat(resultado.getAvaliado()).isSameAs(comprador);
        verify(comprador).setReputacaoMedia(5.0);
        verify(comprador).setTotalAvaliacoes(1);
        verify(usuarioRepository).save(comprador);
    }
}
