package br.com.novaroupa.repository;

import br.com.novaroupa.model.TipoTecido;

/** Resultado tipado da agregação de peso por categoria de tecido. */
public interface CategoriaImpactoProjection {
    TipoTecido getCategoria();
    Double getQuilos();
}
