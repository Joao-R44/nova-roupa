package br.com.novaroupa.controller;

import br.com.novaroupa.dto.PropostaCadastroDTO;
import br.com.novaroupa.dto.PropostaResponseDTO;
import br.com.novaroupa.dto.PropostaStatusDTO;
import br.com.novaroupa.service.PropostaNegociacaoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/** Endpoints de envio, consulta e decisão de propostas de negociação. */
@RestController
@RequestMapping("/api/propostas")
public class PropostaNegociacaoController {
    private final PropostaNegociacaoService service;

    public PropostaNegociacaoController(PropostaNegociacaoService service) {
        this.service = service;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('COMPRADOR', 'ADMIN')")
    public ResponseEntity<PropostaResponseDTO> criar(@Valid @RequestBody PropostaCadastroDTO dados,
                                                      Authentication authentication) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(dados, authentication.getName()));
    }

    @GetMapping("/minhas")
    @PreAuthorize("hasAnyRole('COMPRADOR', 'ADMIN')")
    public List<PropostaResponseDTO> minhas(Authentication authentication) {
        return service.minhas(authentication.getName());
    }

    @GetMapping("/recebidas")
    @PreAuthorize("hasAnyRole('VENDEDOR', 'ADMIN')")
    public List<PropostaResponseDTO> recebidas(Authentication authentication) {
        return service.recebidas(authentication.getName());
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('VENDEDOR', 'ADMIN')")
    public PropostaResponseDTO atualizarStatus(@PathVariable Long id,
                                                 @Valid @RequestBody PropostaStatusDTO dados,
                                                 Authentication authentication) {
        return service.atualizarStatus(id, dados, authentication.getName());
    }
}
