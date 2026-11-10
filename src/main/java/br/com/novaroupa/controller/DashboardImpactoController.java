package br.com.novaroupa.controller;

import br.com.novaroupa.dto.DashboardImpactoDTO;
import br.com.novaroupa.service.DashboardImpactoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Endpoint público de indicadores socioambientais agregados. */
@RestController
@RequestMapping("/api/dashboard")
public class DashboardImpactoController {

    private final DashboardImpactoService dashboardImpactoService;

    public DashboardImpactoController(DashboardImpactoService dashboardImpactoService) {
        this.dashboardImpactoService = dashboardImpactoService;
    }

    @GetMapping("/impacto")
    public DashboardImpactoDTO obterImpacto() {
        return dashboardImpactoService.obterImpacto();
    }
}
