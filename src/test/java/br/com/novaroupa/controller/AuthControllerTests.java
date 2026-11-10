package br.com.novaroupa.controller;

import br.com.novaroupa.model.TipoPerfil;
import br.com.novaroupa.model.Usuario;
import br.com.novaroupa.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("dev")
@TestPropertySource(properties = "spring.datasource.url=jdbc:sqlite:target/test-extensao.db")
class AuthControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    void loginReturnsJwtWithRoleAndTokenAuthorizesProtectedPaths() throws Exception {
        MvcResult result = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"username":"vendedor","password":"vendedor-demo-123"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type").value("Bearer"))
                .andExpect(jsonPath("$.username").value("vendedor"))
                .andExpect(jsonPath("$.roles[0]").value("ROLE_VENDEDOR"))
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.expiresIn").value(3_600_000))
                .andReturn();

        Usuario perfil = usuarioRepository.findByUsername("vendedor").orElseThrow();
        assertThat(perfil.getTipoPerfil()).isEqualTo(TipoPerfil.VENDEDOR);
        assertThat(perfil.getReputacaoMedia()).isEqualTo(0.0);
        assertThat(perfil.getTotalAvaliacoes()).isZero();
        assertThat(perfil.getDataCadastro()).isNotNull();

        String token = com.jayway.jsonpath.JsonPath.read(result.getResponse().getContentAsString(), "$.token");
        // A rota de teste não existe: 404 confirma que o filtro aceitou o JWT antes do controller.
        mockMvc.perform(get("/api/rota-ainda-nao-implementada").header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound());
    }

    @Test
    void invalidCredentialsReturnGenericUnauthorizedMessage() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"username":"vendedor","password":"incorreta"}
                                """))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Credenciais inválidas"));
    }

    @Test
    void loginRateLimitReturnsTooManyRequestsAfterFiveAttemptsFromSameOrigin() throws Exception {
        RequestPostProcessor origin = request -> {
            request.setRemoteAddr("198.51.100.40");
            return request;
        };

        for (int attempt = 0; attempt < 5; attempt++) {
            mockMvc.perform(post("/api/auth/login").with(origin)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                    {"username":"vendedor","password":"incorreta"}
                                    """))
                    .andExpect(status().isUnauthorized());
        }

        mockMvc.perform(post("/api/auth/login").with(origin)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"username":"vendedor","password":"incorreta"}
                                """))
                .andExpect(status().isTooManyRequests())
                .andExpect(jsonPath("$.message").value(
                        "Muitas tentativas de login. Tente novamente mais tarde."))
                .andExpect(result -> assertThat(result.getResponse().getHeader("Retry-After")).isNotBlank());
    }

    @Test
    void blankCredentialsAreRejectedByBeanValidation() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"username":" ","password":""}
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void corsAllowsConfiguredLocalFrontendOrigin() throws Exception {
        mockMvc.perform(options("/api/lotes")
                        .header("Origin", "http://localhost:4200")
                        .header("Access-Control-Request-Method", "GET")
                        .header("Access-Control-Request-Headers", "authorization,content-type"))
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Origin", "http://localhost:4200"));
    }

    @Test
    void corsRejectsOriginNotInAllowlist() throws Exception {
        mockMvc.perform(options("/api/lotes")
                        .header("Origin", "https://untrusted.example")
                        .header("Access-Control-Request-Method", "GET"))
                .andExpect(status().isForbidden());
    }

    @Test
    void openApiSpecificationIsPublicAndContainsProjectMetadata() throws Exception {
        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.info.title").value("Nova Roupa API"))
                .andExpect(jsonPath("$.info.version").value("0.1.0"));
    }

    @Test
    void actuatorHealthAndProbesArePublicButInfoRequiresAuthentication() throws Exception {
        mockMvc.perform(get("/actuator/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"));
        mockMvc.perform(get("/actuator/health/liveness"))
                .andExpect(status().isOk());
        mockMvc.perform(get("/actuator/info"))
                .andExpect(status().isForbidden());
    }
}
