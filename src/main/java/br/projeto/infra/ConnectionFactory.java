package br.projeto.infra;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public final class ConnectionFactory {
    private static final Properties PROPS = new Properties();

    static {
        try (InputStream in = ConnectionFactory.class
                .getClassLoader()
                .getResourceAsStream("db.properties")) {
            if (in == null) throw new IllegalStateException("db.properties não encontrado (src/main/resources)");
            PROPS.load(in);
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (Exception e) {
            throw new ExceptionInInitializerError("Falha ao inicializar ConnectionFactory: " + e.getMessage());
        }
    }

    private ConnectionFactory() {}

    public static Connection getConnection() throws Exception {
        String url  = PROPS.getProperty("db.url");
        String user = PROPS.getProperty("db.user");
        String pass = PROPS.getProperty("db.pass");
        return DriverManager.getConnection(url, user, pass);
    }
}
