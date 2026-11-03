package br.com.novaroupa.specification;

import br.com.novaroupa.model.LoteRetalho;
import br.com.novaroupa.model.StatusLote;
import br.com.novaroupa.model.TipoTecido;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/** Filtros combináveis para a consulta pública de lotes disponíveis. */
public final class LoteSpecification {

    private LoteSpecification() {
    }

    public static Specification<LoteRetalho> comFiltros(TipoTecido tipoTecido, String cor,
                                                         Double pesoMin, Double pesoMax,
                                                         String localizacao) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.equal(root.get("status"), StatusLote.DISPONIVEL));

            if (tipoTecido != null) {
                predicates.add(criteriaBuilder.equal(root.get("tipoTecido"), tipoTecido));
            }
            if (cor != null && !cor.isBlank()) {
                String valor = "%" + cor.trim().toLowerCase(Locale.ROOT) + "%";
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("corPredominante")), valor));
            }
            if (pesoMin != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.<Double>get("pesoKg"), pesoMin));
            }
            if (pesoMax != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.<Double>get("pesoKg"), pesoMax));
            }
            if (localizacao != null && !localizacao.isBlank()) {
                String valor = "%" + localizacao.trim().toLowerCase(Locale.ROOT) + "%";
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("localizacao")), valor));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
