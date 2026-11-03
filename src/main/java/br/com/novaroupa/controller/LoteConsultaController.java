package br.com.novaroupa.controller;

import br.com.novaroupa.dto.LoteConsultaDTO;
import br.com.novaroupa.model.TipoTecido;
import br.com.novaroupa.service.LoteConsultaService;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** Endpoint público para pesquisar lotes disponíveis no catálogo. */
@RestController
@RequestMapping("/api/lotes")
@Validated
public class LoteConsultaController {

    private final LoteConsultaService loteConsultaService;

    public LoteConsultaController(LoteConsultaService loteConsultaService) {
        this.loteConsultaService = loteConsultaService;
    }

    @GetMapping
    public Page<LoteConsultaDTO> listar(
            @RequestParam(required = false) TipoTecido tipoTecido,
            @RequestParam(required = false) String cor,
            @RequestParam(required = false) @Min(0) Double pesoMin,
            @RequestParam(required = false) @Min(0) Double pesoMax,
            @RequestParam(required = false) String localizacao,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) int size) {
        return loteConsultaService.buscar(tipoTecido, cor, pesoMin, pesoMax,
                localizacao, page, size);
    }
}
