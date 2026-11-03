-- O índice único impede usernames duplicados também no SQLite.
CREATE UNIQUE INDEX IF NOT EXISTS uk_usuarios_username ON usuarios (username);

-- Índices usados nas consultas de lotes por tipo, cor e status.
CREATE INDEX IF NOT EXISTS idx_lotes_retalho_tipo_tecido ON lotes_retalho (tipo_tecido);
CREATE INDEX IF NOT EXISTS idx_lotes_retalho_cor_predominante ON lotes_retalho (cor_predominante);
CREATE INDEX IF NOT EXISTS idx_lotes_retalho_status ON lotes_retalho (status);
