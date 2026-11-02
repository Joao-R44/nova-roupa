-- O índice único impede usernames duplicados também no SQLite.
CREATE UNIQUE INDEX IF NOT EXISTS uk_usuarios_username ON usuarios (username);
