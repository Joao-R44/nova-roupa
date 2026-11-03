-- O índice único impede usernames duplicados também no SQLite.
CREATE UNIQUE INDEX IF NOT EXISTS uk_usuarios_username ON usuarios (username);

-- O índice único bloqueia avaliações repetidas do mesmo avaliador na mesma proposta.
CREATE UNIQUE INDEX IF NOT EXISTS uk_avaliacao_proposta_avaliador
    ON avaliacoes_parceiro (proposta_id, avaliador_id);

-- Índices usados nas consultas de lotes por tipo, cor e status.
CREATE INDEX IF NOT EXISTS idx_lotes_retalho_tipo_tecido ON lotes_retalho (tipo_tecido);
CREATE INDEX IF NOT EXISTS idx_lotes_retalho_cor_predominante ON lotes_retalho (cor_predominante);
CREATE INDEX IF NOT EXISTS idx_lotes_retalho_status ON lotes_retalho (status);
