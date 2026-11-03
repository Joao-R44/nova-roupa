package br.com.novaroupa.repository;

import br.com.novaroupa.model.LoteRetalho;
import br.com.novaroupa.model.StatusLote;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;

/** Acesso aos lotes de retalhos persistidos. */
public interface LoteRetalhoRepository extends JpaRepository<LoteRetalho, Long>, JpaSpecificationExecutor<LoteRetalho> {

    @Query("select coalesce(sum(l.pesoKg), 0) from LoteRetalho l where l.status in :statuses")
    double sumPesoKgByStatusIn(Collection<StatusLote> statuses);

    @Query("select count(distinct l.vendedor.id) from LoteRetalho l where l.status in :statuses")
    long countDistinctVendedoresByStatusIn(Collection<StatusLote> statuses);

    @Query("select l.tipoTecido as categoria, sum(l.pesoKg) as quilos "
            + "from LoteRetalho l where l.status in :statuses "
            + "group by l.tipoTecido order by sum(l.pesoKg) desc")
    List<CategoriaImpactoProjection> findCategoriasByStatusIn(Collection<StatusLote> statuses, Pageable pageable);
}
