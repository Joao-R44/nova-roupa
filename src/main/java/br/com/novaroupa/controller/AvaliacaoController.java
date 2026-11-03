package br.com.novaroupa.controller;

import br.com.novaroupa.dto.AvaliacaoDTO;
import br.com.novaroupa.dto.AvaliacaoRespostaDTO;
import br.com.novaroupa.service.AvaliacaoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Endpoint autenticado para avaliações após a conclusão de uma negociação. */
@RestController
@RequestMapping("/api/avaliacoes")
public class AvaliacaoController {

    private final AvaliacaoService avaliacaoService;

    public AvaliacaoController(AvaliacaoService avaliacaoService) {
        this.avaliacaoService = avaliacaoService;
    }

    @PostMapping
    public ResponseEntity<AvaliacaoRespostaDTO> registrar(@Valid @RequestBody AvaliacaoDTO dados,
                                                           Authentication authentication) {
        var username = authentication == null ? "anonymous" : authentication.getName();
        var avaliacao = avaliacaoService.registrar(dados, username);
        return ResponseEntity.status(HttpStatus.CREATED).body(AvaliacaoRespostaDTO.from(avaliacao));
    }
}
