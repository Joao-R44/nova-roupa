package br.com.novaroupa.controller;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("dev")
@TestPropertySource(properties = "spring.datasource.url=jdbc:sqlite:target/test-dashboard-impacto.db")
class DashboardImpactoControllerTests {

    @Autowired private MockMvc mockMvc;
    @Autowired private LoteRetalhoRepository loteRepository;
    @Autowired private PropostaNegociacaoRepository propostaRepository;
    @Autowired private UsuarioRepository usuarioRepository;

    @BeforeEach
    void limparDados() {
        propostaRepository.deleteAll();
        loteRepository.deleteAll();
        usuarioRepository.deleteAll();
    }

    @Test
    void agregaReusoNegociacoesPessoasECategorias() throws Exception {
        Usuario vendedor = usuarioRepository.save(new Usuario("vendedor-dashboard", TipoPerfil.VENDEDOR));
        Usuario comprador = usuarioRepository.save(new Usuario("comprador-dashboard", TipoPerfil.COMPRADOR));
        LoteRetalho algodao = salvarLote(vendedor, TipoTecido.ALGODAO, 310.0, StatusLote.FINALIZADO);
        salvarLote(vendedor, TipoTecido.JEANS, 150.5, StatusLote.DOADO);
        salvarLote(vendedor, TipoTecido.VISCOSE, 12.0, StatusLote.DISPONIVEL);
        PropostaNegociacao proposta = propostaRepository.save(
                new PropostaNegociacao(algodao, comprador, "negociação concluída", null));
        proposta.setStatus(StatusPropostaNegociacao.CONCLUIDA);
        propostaRepository.save(proposta);

        mockMvc.perform(get("/api/dashboard/impacto"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalQuilosReaproveitados").value(460.5))
                .andExpect(jsonPath("$.totalLotesNegociados").value(1))
                .andExpect(jsonPath("$.produtoresBeneficiados").value(1))
                .andExpect(jsonPath("$.compradoresAtivos").value(1))
                .andExpect(jsonPath("$.estimativaReducaoCarbonoKg").value(690.75))
                .andExpect(jsonPath("$.topCategorias[0].categoria").value("ALGODAO"))
                .andExpect(jsonPath("$.topCategorias[0].quilos").value(310.0))
                .andExpect(jsonPath("$.topCategorias[1].categoria").value("JEANS"));
    }

    private LoteRetalho salvarLote(Usuario vendedor, TipoTecido tipo, double peso, StatusLote status) {
        LoteRetalho lote = new LoteRetalho(vendedor, tipo, peso, "Multicolor", "Caruaru, PE");
        lote.setStatus(status);
        ReflectionTestUtils.setField(lote, "dataPublicacao", java.time.LocalDateTime.now());
        return loteRepository.save(lote);
    }
}
