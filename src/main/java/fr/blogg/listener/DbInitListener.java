package fr.blogg.listener;

import fr.blogg.util.DbHelper;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Au démarrage : charge le driver JDBC (PostgreSQL par défaut), initialise la connexion,
 * crée les tables si nécessaire, insère l'admin et le premier article.
 * PostgreSQL : très stable, puissant, bon support cloud.
 */
public class DbInitListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        String driver = sce.getServletContext().getInitParameter("jdbc.driver");
        if (driver == null || driver.isBlank()) {
            driver = "org.postgresql.Driver";
        }
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Driver JDBC introuvable : " + driver + ". Ajoutez le JAR (ex. postgresql-42.7.1.jar) dans WEB-INF/lib", e);
        }
        DbHelper.init(sce.getServletContext());

        try (Connection conn = DbHelper.getConnection();
             Statement st = conn.createStatement()) {

            String url = conn.getMetaData().getURL();
            boolean isPostgres = url.contains("postgresql");
            boolean isMySQL = url.contains("mysql");

            if (isPostgres) {
                createTablesPostgreSQL(st);
            } else if (isMySQL) {
                createTablesMySQL(st);
            } else {
                createTablesH2(st);
            }

            // Données initiales : admin et premier article (seulement si vide)
            try (ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM member")) {
                if (rs.next() && rs.getInt(1) == 0) {
                    if (isPostgres || !isMySQL) {
                        st.executeUpdate("""
                            INSERT INTO member (id, email, password_hash, pseudo, email_validated, role)
                            VALUES (1, 'admin@blogg.fr', 'admin', 'Admin', TRUE, 'ADMIN')
                            """);
                        st.executeUpdate("""
                            INSERT INTO article (id, author_id, author_pseudo, title, content, created_at, updated_at)
                            VALUES (1, 1, 'Admin', 'Bienvenue sur le blog',
                                'Ceci est le premier article. Utilisez l''espace membres pour écrire et commenter.',
                                CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
                            """);
                        if (isPostgres) {
                            st.execute("SELECT setval(pg_get_serial_sequence('member', 'id'), (SELECT COALESCE(MAX(id), 1) FROM member))");
                            st.execute("SELECT setval(pg_get_serial_sequence('article', 'id'), (SELECT COALESCE(MAX(id), 1) FROM article))");
                        }
                    } else {
                        st.executeUpdate("""
                            INSERT INTO member (id, email, password_hash, pseudo, email_validated, role)
                            VALUES (1, 'admin@blogg.fr', 'admin', 'Admin', TRUE, 'ADMIN')
                            """);
                        st.executeUpdate("""
                            INSERT INTO article (id, author_id, author_pseudo, title, content, created_at, updated_at)
                            VALUES (1, 1, 'Admin', 'Bienvenue sur le blog',
                                'Ceci est le premier article. Utilisez l''espace membres pour écrire et commenter.',
                                NOW(), NOW())
                            """);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Erreur d'initialisation de la base de données. Vérifiez que PostgreSQL est démarré, que la base 'blogg' existe et que jdbc.password dans web.xml est correct.", e);
        }
    }

    private void createTablesPostgreSQL(Statement st) throws java.sql.SQLException {
        st.execute("""
            CREATE TABLE IF NOT EXISTS member (
                id BIGSERIAL PRIMARY KEY,
                email VARCHAR(255) NOT NULL UNIQUE,
                password_hash VARCHAR(255),
                pseudo VARCHAR(100),
                email_validated BOOLEAN DEFAULT FALSE,
                validation_token VARCHAR(255),
                role VARCHAR(50) DEFAULT 'MEMBER',
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
            """);
        st.execute("CREATE INDEX IF NOT EXISTS idx_member_email ON member(email)");
        st.execute("""
            CREATE TABLE IF NOT EXISTS profile (
                id BIGSERIAL PRIMARY KEY,
                member_id BIGINT NOT NULL UNIQUE REFERENCES member(id) ON DELETE CASCADE,
                display_name VARCHAR(255),
                bio TEXT,
                avatar_path VARCHAR(500)
            )
            """);
        st.execute("""
            CREATE TABLE IF NOT EXISTS article (
                id BIGSERIAL PRIMARY KEY,
                author_id BIGINT NOT NULL REFERENCES member(id) ON DELETE CASCADE,
                author_pseudo VARCHAR(100),
                title VARCHAR(500) NOT NULL,
                content TEXT,
                image_path VARCHAR(500),
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
            """);
        st.execute("""
            CREATE TABLE IF NOT EXISTS comment (
                id BIGSERIAL PRIMARY KEY,
                article_id BIGINT NOT NULL REFERENCES article(id) ON DELETE CASCADE,
                author_id BIGINT,
                author_pseudo VARCHAR(100),
                content TEXT NOT NULL,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
            """);
    }

    private void createTablesMySQL(Statement st) throws java.sql.SQLException {
        st.execute("""
            CREATE TABLE IF NOT EXISTS member (
                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                email VARCHAR(255) NOT NULL UNIQUE,
                password_hash VARCHAR(255),
                pseudo VARCHAR(100),
                email_validated TINYINT(1) DEFAULT 0,
                validation_token VARCHAR(255),
                role VARCHAR(50) DEFAULT 'MEMBER',
                created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                INDEX idx_email (email)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4
            """);
        st.execute("""
            CREATE TABLE IF NOT EXISTS profile (
                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                member_id BIGINT NOT NULL UNIQUE,
                display_name VARCHAR(255),
                bio LONGTEXT,
                avatar_path VARCHAR(500),
                FOREIGN KEY (member_id) REFERENCES member(id) ON DELETE CASCADE
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4
            """);
        st.execute("""
            CREATE TABLE IF NOT EXISTS article (
                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                author_id BIGINT NOT NULL,
                author_pseudo VARCHAR(100),
                title VARCHAR(500) NOT NULL,
                content LONGTEXT,
                image_path VARCHAR(500),
                created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                FOREIGN KEY (author_id) REFERENCES member(id) ON DELETE CASCADE
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4
            """);
        st.execute("""
            CREATE TABLE IF NOT EXISTS comment (
                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                article_id BIGINT NOT NULL,
                author_id BIGINT,
                author_pseudo VARCHAR(100),
                content LONGTEXT NOT NULL,
                created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY (article_id) REFERENCES article(id) ON DELETE CASCADE
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4
            """);
    }

    private void createTablesH2(Statement st) throws java.sql.SQLException {
        st.execute("""
            CREATE TABLE IF NOT EXISTS member (
                id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                email VARCHAR(255) NOT NULL UNIQUE,
                password_hash VARCHAR(255),
                pseudo VARCHAR(100),
                email_validated BOOLEAN DEFAULT FALSE,
                validation_token VARCHAR(255),
                role VARCHAR(50) DEFAULT 'MEMBER',
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
            """);
        st.execute("""
            CREATE TABLE IF NOT EXISTS profile (
                id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                member_id BIGINT NOT NULL UNIQUE,
                display_name VARCHAR(255),
                bio CLOB,
                avatar_path VARCHAR(500),
                FOREIGN KEY (member_id) REFERENCES member(id) ON DELETE CASCADE
            )
            """);
        st.execute("""
            CREATE TABLE IF NOT EXISTS article (
                id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                author_id BIGINT NOT NULL,
                author_pseudo VARCHAR(100),
                title VARCHAR(500) NOT NULL,
                content CLOB,
                image_path VARCHAR(500),
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY (author_id) REFERENCES member(id) ON DELETE CASCADE
            )
            """);
        st.execute("""
            CREATE TABLE IF NOT EXISTS comment (
                id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                article_id BIGINT NOT NULL,
                author_id BIGINT,
                author_pseudo VARCHAR(100),
                content CLOB NOT NULL,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY (article_id) REFERENCES article(id) ON DELETE CASCADE
            )
            """);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }
}
