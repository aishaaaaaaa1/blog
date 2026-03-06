package fr.blogg.util;

import jakarta.servlet.ServletContext;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Fournit les connexions JDBC à la base H2.
 * L'URL est enregistrée au démarrage par DbInitListener.
 */
public final class DbHelper {

    private static volatile String jdbcUrl;
    private static volatile String jdbcUser;
    private static volatile String jdbcPassword;

    private DbHelper() {}

    public static void init(ServletContext ctx) {
        String envUrl = System.getenv("JDBC_URL");
        String envUser = System.getenv("JDBC_USER");
        String envPass = System.getenv("JDBC_PASSWORD");

        if (envUrl != null && !envUrl.isBlank()) {
            jdbcUrl = envUrl;
            jdbcUser = nonBlank(envUser, "postgres");
            jdbcPassword = envPass != null ? envPass : "";
        } else {
            String url = ctx.getInitParameter("jdbc.url");
            if (url == null || url.isBlank()) {
                url = "jdbc:postgresql://localhost:5432/blogg";
            }
            jdbcUrl = url;
            jdbcUser = nonBlank(ctx.getInitParameter("jdbc.user"), "postgres");
            jdbcPassword = ctx.getInitParameter("jdbc.password");
            if (jdbcPassword == null) jdbcPassword = "";
        }
    }

    private static String nonBlank(String v, String def) {
        return (v != null && !v.isBlank()) ? v : def;
    }

    public static Connection getConnection() throws SQLException {
        if (jdbcUrl == null) {
            throw new IllegalStateException("DbHelper non initialisé (DbInitListener doit être déclaré dans web.xml)");
        }
        return DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPassword);
    }
}
