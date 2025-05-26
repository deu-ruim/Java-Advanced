-- Create a sequence
CREATE SEQUENCE usuarios_seq START WITH 1 INCREMENT BY 1;

-- Modify the table to auto-increment using the sequence
CREATE TABLE usuarios (
    id NUMBER DEFAULT usuarios_seq.nextval PRIMARY KEY,
    email VARCHAR2(255) NOT NULL UNIQUE,
    username VARCHAR2(50) NOT NULL UNIQUE,
    password VARCHAR2(255) NOT NULL
);


select * from usuarios;