CREATE DATABASE IF NOT EXISTS catalogo DEFAULT CHARACTER SET utf8mb4;
USE catalogo;

DROP TABLE IF EXISTS midia;
CREATE TABLE midia (
  id         INT AUTO_INCREMENT PRIMARY KEY,
  titulo     VARCHAR(200) NOT NULL,
  autor      VARCHAR(200) NOT NULL,
  tipo       ENUM('FILME','LIVRO') NOT NULL,
  ano        INT,
  nota       DECIMAL(3,1),
  sinopse    TEXT,
  poster_url VARCHAR(500)
);

INSERT INTO midia (titulo, autor, tipo, ano, nota)
VALUES ('O Senhor dos Anéis','J.R.R. Tolkien','LIVRO',1954,5),
       ('Matrix','Wachowski','FILME',1999,5);
