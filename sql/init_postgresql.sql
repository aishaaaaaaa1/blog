-- ============================================================
-- Script PostgreSQL pour le blog (base "blogg")
-- ============================================================
--
-- ÉTAPE 1 : Connecte-toi à la base "postgres" (utilisateur postgres).
--           Exécute uniquement la commande ci-dessous (CREATE DATABASE).
--
-- ÉTAPE 2 : Connecte-toi à la base "blogg", puis exécute tout le reste
--           du fichier (à partir de "CREATE TABLE IF NOT EXISTS member").
--
-- Dans psql : après CREATE DATABASE, tape  \c blogg   puis exécute le reste.
-- Dans pgAdmin : requête 1 = CREATE DATABASE ; puis ouvrir "blogg" et exécuter le reste.
-- ============================================================

-- 1. Créer la base (connecté à "postgres")
CREATE DATABASE blogg
  ENCODING 'UTF8'
  LC_COLLATE 'French_France.1252'
  LC_CTYPE 'French_France.1252'
  TEMPLATE template0;

-- ========== À partir d'ici : se connecter à la base "blogg" puis exécuter ==========

-- 2. Tables (l'appli peut les créer au démarrage ; ce script permet de tout faire à la main si besoin)

CREATE TABLE IF NOT EXISTS member (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255),
    pseudo VARCHAR(100),
    email_validated BOOLEAN DEFAULT FALSE,
    validation_token VARCHAR(255),
    role VARCHAR(50) DEFAULT 'MEMBER',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_member_email ON member(email);

CREATE TABLE IF NOT EXISTS profile (
    id BIGSERIAL PRIMARY KEY,
    member_id BIGINT NOT NULL UNIQUE REFERENCES member(id) ON DELETE CASCADE,
    display_name VARCHAR(255),
    bio TEXT,
    avatar_path VARCHAR(500)
);

CREATE TABLE IF NOT EXISTS article (
    id BIGSERIAL PRIMARY KEY,
    author_id BIGINT NOT NULL REFERENCES member(id) ON DELETE CASCADE,
    author_pseudo VARCHAR(100),
    title VARCHAR(500) NOT NULL,
    content TEXT,
    image_path VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS comment (
    id BIGSERIAL PRIMARY KEY,
    article_id BIGINT NOT NULL REFERENCES article(id) ON DELETE CASCADE,
    author_id BIGINT,
    author_pseudo VARCHAR(100),
    content TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 3. Données initiales (seulement si la table member est vide)
INSERT INTO member (id, email, password_hash, pseudo, email_validated, role)
SELECT 1, 'admin@blogg.fr', 'admin', 'Admin', TRUE, 'ADMIN'
WHERE NOT EXISTS (SELECT 1 FROM member LIMIT 1);

INSERT INTO article (id, author_id, author_pseudo, title, content, created_at, updated_at)
SELECT 1, 1, 'Admin', 'Bienvenue sur le blog',
    'Ceci est le premier article. Utilisez l''espace membres pour écrire et commenter.',
    CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM article LIMIT 1);

-- Remettre les séquences au bon niveau (après insertion avec id=1)
SELECT setval(pg_get_serial_sequence('member', 'id'), (SELECT COALESCE(MAX(id), 1) FROM member));
SELECT setval(pg_get_serial_sequence('article', 'id'), (SELECT COALESCE(MAX(id), 1) FROM article));
