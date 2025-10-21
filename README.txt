# Catálogo de Mídias (Java/Servlets/JSP/JDBC)

Aplicação web para cadastro, listagem, edição, exclusão e busca de **livros** e **filmes**.  
Tecnologias: Java 25, Maven, Tomcat 9, MySQL 8, JDBC, JSP/JSTL.

## Como rodar

1. **Pré-requisitos**: JDK 25, Maven 3.9+, Tomcat 9, MySQL 8.
2. **Banco** (MySQL):
   ```sql
   CREATE DATABASE catalogo DEFAULT CHARACTER SET utf8mb4;
   USE catalogo;
   -- Tabela (colunas usadas pela app)
   CREATE TABLE IF NOT EXISTS midia (
     id INT AUTO_INCREMENT PRIMARY KEY,
     titulo VARCHAR(200) NOT NULL,
     autor VARCHAR(200),
     tipo ENUM('FILME','LIVRO') NOT NULL,
     ano INT,
     nota DECIMAL(3,1),
     sinopse TEXT,
     poster_url VARCHAR(300)
   );
