package br.com.novaroupa.repository;

import br.com.novaroupa.model.PropostaNegociacao;
import br.com.novaroupa.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/** Acesso às propostas de negociação persistidas. */
public interface PropostaNegociacaoRepository extends JpaRepository<PropostaNegociacao, Long> {

    List<PropostaNegociacao> findByComprador(Usuario comprador);

    List<PropostaNegociacao> findByLoteVendedor(Usuario vendedor);
}
