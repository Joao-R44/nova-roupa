package br.com.novaroupa.dto;

import java.util.List;

/** Métricas agregadas para o painel de impacto socioambiental. */
public record DashboardImpactoDTO(
        double totalQuilosReaproveitados,
        long totalLotesNegociados,
        long produtoresBeneficiados,
        long compradoresAtivos,
        double estimativaReducaoCarbonoKg,
        List<CategoriaImpactoDTO> topCategorias) {

    public record CategoriaImpactoDTO(String categoria, double quilos) {
    }
}
