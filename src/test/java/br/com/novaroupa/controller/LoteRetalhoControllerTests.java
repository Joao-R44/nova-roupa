package br.com.novaroupa.controller;

import br.com.novaroupa.repository.LoteRetalhoRepository;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("dev")
@TestPropertySource(properties = "spring.datasource.url=jdbc:sqlite:target/test-extensao.db")
class LoteRetalhoControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LoteRetalhoRepository loteRetalhoRepository;

    @BeforeEach
    void limparLotesDeTestes() {
        loteRetalhoRepository.deleteAll();
    }

    @Test
    void vendedorAutenticadoPodeCadastrarLoteEFicaVinculadoAoSeuPerfil() throws Exception {
        String token = obterToken("vendedor", "vendedor-demo-123");

        MvcResult resultado = mockMvc.perform(post("/api/lotes")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(corpoValido()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.vendedor").value("vendedor"))
                .andExpect(jsonPath("$.tipoTecido").value("ALGODAO"))
                .andExpect(jsonPath("$.status").value("DISPONIVEL"))
                .andReturn();

        Long id = ((Number) JsonPath.read(resultado.getResponse().getContentAsString(), "$.id")).longValue();
        assertThat(loteRetalhoRepository.findById(id)).isPresent();
        assertThat(loteRetalhoRepository.count()).isEqualTo(1);
    }

    @Test
    void corpoComCamposObrigatoriosAusentesRetornaBadRequest() throws Exception {
        String token = obterToken("vendedor", "vendedor-demo-123");

        mockMvc.perform(post("/api/lotes")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"tipoTecido":"ALGODAO","corPredominante":"azul","localizacao":"Recife"}
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void compradorNaoPodeCadastrarLote() throws Exception {
        String token = obterToken("comprador", "comprador-demo-123");

        mockMvc.perform(post("/api/lotes")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(corpoValido()))
                .andExpect(status().isForbidden());
    }

    private String obterToken(String usuario, String senha) throws Exception {
        MvcResult resultado = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"" + usuario + "\",\"password\":\"" + senha + "\"}"))
                .andExpect(status().isOk())
                .andReturn();
        return JsonPath.read(resultado.getResponse().getContentAsString(), "$.token");
    }

    private String corpoValido() {
        return """
                {
                  "tipoTecido":"ALGODAO",
                  "pesoKg":4.5,
                  "metragemAproximada":12.0,
                  "corPredominante":"Azul",
                  "descricao":"Retalhos de algodão",
                  "fotosUrls":"https://exemplo.test/foto.jpg",
                  "localizacao":"Recife"
                }
                """;
    }
}
