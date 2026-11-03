package br.com.novaroupa.controller;

import br.com.novaroupa.model.AvaliacaoParceiro;
import br.com.novaroupa.security.JwtTokenProvider;
import br.com.novaroupa.service.AvaliacaoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AvaliacaoController.class)
@AutoConfigureMockMvc(addFilters = false)
class AvaliacaoControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AvaliacaoService avaliacaoService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @Test
    void registraAvaliacaoComDadosValidos() throws Exception {
        AvaliacaoParceiro avaliacao = mock(AvaliacaoParceiro.class);
        when(avaliacao.getId()).thenReturn(1L);
        when(avaliacao.getProposta()).thenReturn(mock(br.com.novaroupa.model.PropostaNegociacao.class));
        when(avaliacao.getProposta().getId()).thenReturn(10L);
        when(avaliacao.getAvaliador()).thenReturn(mock(br.com.novaroupa.model.Usuario.class));
        when(avaliacao.getAvaliador().getId()).thenReturn(2L);
        when(avaliacao.getAvaliado()).thenReturn(mock(br.com.novaroupa.model.Usuario.class));
        when(avaliacao.getAvaliado().getId()).thenReturn(3L);
        when(avaliacao.getNota()).thenReturn(5);
        when(avaliacao.getDataCriacao()).thenReturn(java.time.LocalDateTime.now());
        when(avaliacaoService.registrar(any(), eq("anonymous"))).thenReturn(avaliacao);

        mockMvc.perform(post("/api/avaliacoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"propostaId\":10,\"nota\":5,\"comentario\":\"Ótimo parceiro\"}"))
                .andExpect(status().isCreated());
    }

    @Test
    void rejeitaNotaForaDoIntervalo() throws Exception {
        mockMvc.perform(post("/api/avaliacoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"propostaId\":10,\"nota\":6}"))
                .andExpect(status().isBadRequest());
    }
}
