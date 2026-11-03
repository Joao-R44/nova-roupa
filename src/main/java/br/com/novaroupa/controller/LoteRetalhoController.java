package br.com.novaroupa.controller;

import br.com.novaroupa.dto.LoteCadastroDTO;
import br.com.novaroupa.dto.LoteResponseDTO;
import br.com.novaroupa.service.LoteRetalhoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Endpoints HTTP para publicação de lotes de retalhos. */
@RestController
@RequestMapping("/api/lotes")
public class LoteRetalhoController {

    private final LoteRetalhoService loteRetalhoService;

    public LoteRetalhoController(LoteRetalhoService loteRetalhoService) {
        this.loteRetalhoService = loteRetalhoService;
    }

    /** Publica um lote para o vendedor autenticado; o cliente não escolhe o vendedor. */
    @PostMapping
    @PreAuthorize("hasAnyRole('VENDEDOR', 'ADMIN')")
    public ResponseEntity<LoteResponseDTO> cadastrarLote(
            @Valid @RequestBody LoteCadastroDTO dados, Authentication authentication) {
        LoteResponseDTO lote = loteRetalhoService.cadastrarLote(dados, authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(lote);
    }
}
