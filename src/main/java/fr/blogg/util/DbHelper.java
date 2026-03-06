package fr.blogg.util;

import jakarta.servlet.ServletContext;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DbHelper {

    private static volatile String jdbcUrl;
    private static volatile String jdbcUser;
    private static volatile String jdbcPassword;

    private DbHelper() {}

    public static void init(ServletContext ctx) {
        String envUrl = System.getenv("JDBC_URL");
        String envUser = System.getenv("JDBC_USER");
        String envPass = System.getenv("JDBC_PASSWORD");

        if ((envUrl == null || envUrl.isBlank()) && System.getenv("DATABASE_URL") != null) {
            parseDatabaseUrl(System.getenv("DATABASE_URL"));
            System.out.println("[DbHelper] Connexion via DATABASE_URL -> " + jdbcUrl);
            return;
        }

        if (envUrl != null && !envUrl.isBlank()) {
            jdbcUrl = envUrl;
            jdbcUser = nonBlank(envUser, "postgres");
            jdbcPassword = envPass != null ? envPass : "";
            System.out.println("[DbHelper] Connexion via variables d'env -> " + jdbcUrl);
        } else {
            String url = ctx.getInitParameter("jdbc.url");
            if (url == null || url.isBlank()) {
                url = "jdbc:postgresql://localhost:5432/blogg";
            }
            jdbcUrl = url;
            jdbcUser = nonBlank(ctx.getInitParameter("jdbc.user"), "postgres");
            jdbcPassword = ctx.getInitParameter("jdbc.password");
            if (jdbcPassword == null) jdbcPassword = "";
            System.out.println("[DbHelper] Connexion via web.xml -> " + jdbcUrl);
        }
    }

    private static void parseDatabaseUrl(String databaseUrl) {
        try {
            String cleaned = databaseUrl;
            if (cleaned.startsWith("postgres://")) {
                cleaned = "postgresql://" + cleaned.substring("postgres://".length());
            }
            if (!cleaned.startsWith("postgresql://")) {
                throw new IllegalArgumentException("Format DATABASE_URL inconnu: " + databaseUrl);
            }
            String rest = cleaned.substring("postgresql://".length());
            String userInfo = rest.substring(0, rest.indexOf('@'));
            String hostAndDb = rest.substring(rest.indexOf('@') + 1);

            jdbcUser = userInfo.contains(":") ? userInfo.substring(0, userInfo.indexOf(':')) : userInfo;
            jdbcPassword = userInfo.contains(":") ? userInfo.substring(userInfo.indexOf(':') + 1) : "";
            jdbcUrl = "jdbc:postgresql://" + hostAndDb;
        } catch (Exception e) {
            System.err.println("[DbHelper] Erreur parsing DATABASE_URL: " + e.getMessage());
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
