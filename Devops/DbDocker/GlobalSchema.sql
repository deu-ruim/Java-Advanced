CREATE SEQUENCE usuario_seq START 1 INCREMENT 1;

CREATE TABLE usuarios (
    id INTEGER PRIMARY KEY DEFAULT nextval('usuario_seq'),
    email VARCHAR(255) NOT NULL UNIQUE,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    uf VARCHAR(2) NOT NULL,
    role VARCHAR(20) DEFAULT 'USER' NOT NULL,
    ativo BOOLEAN DEFAULT TRUE NOT NULL
);

COMMENT ON COLUMN usuarios.ativo IS 'User active status (true=ativo, false=inativo)';
COMMENT ON COLUMN usuarios.id IS 'Primary key';
COMMENT ON COLUMN usuarios.email IS 'User email address';
COMMENT ON COLUMN usuarios.username IS 'User login name';
COMMENT ON COLUMN usuarios.password IS 'Encrypted user password';
COMMENT ON COLUMN usuarios.uf IS 'Brazilian state (UF)';
COMMENT ON COLUMN usuarios.role IS 'User role (USER, ADMIN, etc.)';

CREATE SEQUENCE desastre_seq START 1 INCREMENT 1;

CREATE TABLE desastres (
    id INTEGER PRIMARY KEY DEFAULT nextval('desastre_seq'),
    uf VARCHAR(2) NOT NULL,
    titulo VARCHAR(100) NOT NULL,
    descricao VARCHAR(1000) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    severidade VARCHAR(20) NOT NULL,
    usuario_id INTEGER NOT NULL,
    CONSTRAINT fk_desastre_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);

COMMENT ON COLUMN desastres.uf IS 'Brazilian state where disaster occurred';
COMMENT ON COLUMN desastres.titulo IS 'Disaster title/name';
COMMENT ON COLUMN desastres.descricao IS 'Detailed description';
COMMENT ON COLUMN desastres.created_at IS 'Timestamp of creation';
COMMENT ON COLUMN desastres.severidade IS 'Severity classification';
COMMENT ON COLUMN desastres.usuario_id IS 'Creator user reference';