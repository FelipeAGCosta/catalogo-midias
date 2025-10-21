package br.projeto.infra;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Fornece conexões JDBC com o banco MySQL do projeto.
 
 * A URL aponta para o schema {@code catalogo}. Caso o seu usuário/senha/host/porta sejam diferentes, ajuste as constantes {@link #URL}, {@link #USER} e {@link #PASS}.
 
    Exemplo de uso:
 * <pre>
 * try (Connection c = ConnectionFactory.getConnection()) {
 *     // usar a conexão
 * }
 * </pre>
 */
public final class ConnectionFactory {

    /** URL JDBC do MySQL (inclui parâmetros de charset e timezone). */
    private static final String URL =
        "jdbc:mysql://localhost:3306/catalogo"
      + "?useUnicode=true&characterEncoding=UTF-8"
      + "&useSSL=false&allowPublicKeyRetrieval=true"
      + "&serverTimezone=UTC";

    /** Usuário do banco. */
    private static final String USER = "root";
    /** Senha do banco. */
    private static final String PASS = "Costa29042904!";

    private ConnectionFactory() {
        // utilitário estático; não instanciar
    }

    /**
     * Abre e retorna uma conexão com o banco configurado.
     
     * @return conexão JDBC pronta para uso
     * @throws Exception se o driver não for encontrado ou ocorrer falha ao conectar
     */
    public static Connection getConnection() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(URL, USER, PASS);
    }
}
