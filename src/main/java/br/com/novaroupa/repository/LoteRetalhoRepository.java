package br.com.novaroupa.repository;

import br.com.novaroupa.model.LoteRetalho;
import org.springframework.data.jpa.repository.JpaRepository;

/** Acesso aos lotes de retalhos persistidos. */
public interface LoteRetalhoRepository extends JpaRepository<LoteRetalho, Long> {
}
