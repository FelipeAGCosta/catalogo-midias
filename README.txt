Catálogo de Mídias (Filmes/Livros)

Objetivo
Aplicação web Java (Servlets + JSP + JDBC) com CRUD, busca, ordenação, paginação e detalhes com pôster.

Tecnologias
Java 25, Maven 3.9.11, Tomcat 9.0.41, MySQL 8, JDBC (mysql-connector-j 8.0.33), JSP/JSTL.

Banco (MySQL)
1) Executar sql\schema.sql no MySQL Workbench.
2) Ajustar credenciais em src\main\java\br\projeto\infra\ConnectionFactory.java (USER/PASS/URL).

Build & Deploy
mvn clean package
Deploy do WAR: target\catalogo-midias-1.0-SNAPSHOT.war no Tomcat 9.
Acessar: http://localhost:8080/catalogo-midias-1.0-SNAPSHOT/index.jsp

Javadoc
Gerar: mvn javadoc:javadoc
Abrir: target\site\apidocs\index.html
