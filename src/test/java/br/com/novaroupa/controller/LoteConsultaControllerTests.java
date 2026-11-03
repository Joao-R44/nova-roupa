package br.com.novaroupa.controller;

import br.com.novaroupa.model.LoteRetalho;
import br.com.novaroupa.model.StatusLote;
import br.com.novaroupa.model.TipoPerfil;
import br.com.novaroupa.model.TipoTecido;
import br.com.novaroupa.model.Usuario;
import br.com.novaroupa.repository.LoteRetalhoRepository;
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

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("dev")
@TestPropertySource(properties = "spring.datasource.url=jdbc:sqlite:target/test-extensao.db")
class LoteConsultaControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LoteRetalhoRepository loteRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private Usuario vendedor;

    @BeforeEach
    void prepararDados() {
        loteRepository.deleteAll();
        vendedor = usuarioRepository.findByUsername("consulta-lotes-teste")
                .orElseGet(() -> usuarioRepository.save(
                        new Usuario("consulta-lotes-teste", TipoPerfil.VENDEDOR)));
    }

    @Test
    void filtrosCombinadosRetornamApenasLotesDisponiveisCorrespondentes() throws Exception {
        LoteRetalho correspondente = salvar(
                TipoTecido.ALGODAO, "Azul Royal", 50.0, "Caruaru, PE",
                StatusLote.DISPONIVEL, LocalDateTime.now().minusHours(1));
        salvar(TipoTecido.JEANS, "Azul", 50.0, "Caruaru, PE",
                StatusLote.DISPONIVEL, LocalDateTime.now());
        salvar(TipoTecido.ALGODAO, "Azul", 150.0, "Caruaru, PE",
                StatusLote.DISPONIVEL, LocalDateTime.now());
        salvar(TipoTecido.ALGODAO, "Azul", 50.0, "Caruaru, PE",
                StatusLote.RESERVADO, LocalDateTime.now());

        mockMvc.perform(get("/api/lotes")
                        .param("tipoTecido", "ALGODAO")
                        .param("cor", "AZUL")
                        .param("pesoMin", "10")
                        .param("pesoMax", "100")
                        .param("localizacao", "caruaru"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].id").value(correspondente.getId()))
                .andExpect(jsonPath("$.content[0].status").value("DISPONIVEL"))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.number").value(0))
                .andExpect(jsonPath("$.size").value(10));
    }

    @Test
    void buscaSemResultadosRetornaPaginaVaziaComMetadados() throws Exception {
        mockMvc.perform(get("/api/lotes").param("tipoTecido", "VISCOSE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(0))
                .andExpect(jsonPath("$.totalElements").value(0))
                .andExpect(jsonPath("$.totalPages").value(0))
                .andExpect(jsonPath("$.number").value(0))
                .andExpect(jsonPath("$.size").value(10));
    }

    @Test
    void resultadosSaoOrdenadosPorDataDePublicacaoDecrescente() throws Exception {
        salvar(TipoTecido.JEANS, "Preto", 12.0, "Recife",
                StatusLote.DISPONIVEL, LocalDateTime.now().minusDays(1));
        LoteRetalho maisRecente = salvar(TipoTecido.VISCOSE, "Verde", 15.0, "Olinda",
                StatusLote.DISPONIVEL, LocalDateTime.now());

        mockMvc.perform(get("/api/lotes").param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(maisRecente.getId()))
                .andExpect(jsonPath("$.content[0].dataPublicacao").exists());
    }

    private LoteRetalho salvar(TipoTecido tipo, String cor, double peso,
                               String localizacao, StatusLote status, LocalDateTime publicadaEm) {
        LoteRetalho lote = new LoteRetalho(vendedor, tipo, peso, cor, localizacao);
        lote.setStatus(status);
        ReflectionTestUtils.setField(lote, "dataPublicacao", publicadaEm);
        return loteRepository.save(lote);
    }
}
