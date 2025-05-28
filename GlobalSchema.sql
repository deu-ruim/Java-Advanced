-- drop table usuarios;

-- Sequence for ID generation
CREATE SEQUENCE usuario_seq
    START WITH 1
    INCREMENT BY 1
    NOCACHE
    NOCYCLE;

-- Usuarios table
CREATE TABLE usuarios (
    id NUMBER PRIMARY KEY,
    email VARCHAR2(255) NOT NULL,
    username VARCHAR2(50) NOT NULL,
    password VARCHAR2(255) NOT NULL,
    uf VARCHAR2(2) NOT NULL,
    role VARCHAR2(20) DEFAULT 'USER' NOT NULL,
    CONSTRAINT uk_usuario_email UNIQUE (email),
    CONSTRAINT uk_usuario_username UNIQUE (username)
);

ALTER TABLE usuarios ADD (
    ativo NUMBER(1) DEFAULT 1 NOT NULL
);

COMMENT ON COLUMN usuarios.ativo IS 'User active status (1=true, 0=false)';

-- Comment on columns (optional but recommended)
COMMENT ON COLUMN usuarios.id IS 'Primary key';
COMMENT ON COLUMN usuarios.email IS 'User email address';
COMMENT ON COLUMN usuarios.username IS 'User login name';
COMMENT ON COLUMN usuarios.password IS 'Encrypted user password';
COMMENT ON COLUMN usuarios.uf IS 'Brazilian state (UF)';
COMMENT ON COLUMN usuarios.role IS 'User role (USER, ADMIN, etc.)';

-- Trigger for auto-increment ID
CREATE OR REPLACE TRIGGER usuario_before_insert
BEFORE INSERT ON usuarios
FOR EACH ROW
BEGIN
    IF :NEW.id IS NULL THEN
        :NEW.id := usuario_seq.NEXTVAL;
    END IF;
END;
/



-- DESASTRES
CREATE SEQUENCE desastre_seq
    START WITH 1
    INCREMENT BY 1
    NOCACHE
    NOCYCLE;

CREATE TABLE desastres (
    id NUMBER PRIMARY KEY,
    uf VARCHAR2(2) NOT NULL,
    titulo VARCHAR2(100) NOT NULL,
    descricao VARCHAR2(1000) NOT NULL,
    created_at TIMESTAMP DEFAULT SYSTIMESTAMP NOT NULL,
    severidade VARCHAR2(20) NOT NULL,
    usuario_id NUMBER NOT NULL,
    CONSTRAINT fk_desastre_usuario
        FOREIGN KEY (usuario_id)
        REFERENCES usuarios(id)
);

COMMENT ON COLUMN desastres.uf IS 'Brazilian state where disaster occurred';
COMMENT ON COLUMN desastres.titulo IS 'Disaster title/name';
COMMENT ON COLUMN desastres.descricao IS 'Detailed description';
COMMENT ON COLUMN desastres.created_at IS 'Timestamp of creation';
COMMENT ON COLUMN desastres.severidade IS 'Severity classification';
COMMENT ON COLUMN desastres.usuario_id IS 'Creator user reference';

-- Add trigger for auto-increment
CREATE OR REPLACE TRIGGER desastre_before_insert
BEFORE INSERT ON desastres
FOR EACH ROW
BEGIN
    IF :NEW.id IS NULL THEN
        :NEW.id := desastre_seq.NEXTVAL;
    END IF;
END;
/

desc usuarios;
desc desastres;
select * from usuarios;
select * from desastres;
commit;
