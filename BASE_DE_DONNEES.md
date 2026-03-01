# Base de données PostgreSQL

Le blog utilise **PostgreSQL** : très stable, puissant, et un très bon support cloud. Les données restent sur le serveur même si tu fermes ou déplaces le projet.

---

## 1. Installer PostgreSQL

- **Windows** : https://www.postgresql.org/download/windows/  
  (installateur officiel ou EDB)
- Pendant l’installation, note le **mot de passe** du super-utilisateur `postgres`.
- Vérifie que le service **PostgreSQL** est démarré (Services Windows ou `pg_ctl`).

---

## 2. Créer la base `blogg`

Ouvre **pgAdmin**, **psql** ou un autre client PostgreSQL et exécute :

```sql
CREATE DATABASE blogg;
```

(Si tu veux forcer l’encodage UTF-8 : `CREATE DATABASE blogg ENCODING 'UTF8';`)  
Une fois la base créée, l’application crée automatiquement les tables et insère l’admin + le premier article au premier démarrage.

---

## 3. JAR PostgreSQL dans le projet

1. Télécharge **postgresql-42.7.1.jar** (ou une 42.x récente) :  
   https://jdbc.postgresql.org/download/  
   ou https://repo1.maven.org/maven2/org/postgresql/postgresql/42.7.1/postgresql-42.7.1.jar  
2. Place le fichier dans : **`src/main/webapp/WEB-INF/lib/`**
3. Dans Eclipse : clic droit sur le projet → **Refresh** (F5).

---

## 4. Configurer web.xml

Dans **`WEB-INF/web.xml`**, les paramètres JDBC sont déjà pour PostgreSQL. À vérifier ou adapter :

- **`jdbc.url`** : `jdbc:postgresql://localhost:5432/blogg`  
  (change le port si PostgreSQL n’écoute pas sur 5432, ou le host si la base est sur un autre serveur)
- **`jdbc.user`** : `postgres` (ou un utilisateur que tu as créé)
- **`jdbc.password`** : mot de passe de cet utilisateur

Exemple si ton mot de passe est `monMotDePasse` :

```xml
<param-name>jdbc.password</param-name>
<param-value>monMotDePasse</param-value>
```

---

## 5. Résumé

| Étape | Action |
|-------|--------|
| 1 | PostgreSQL installé et service démarré |
| 2 | Base `blogg` créée (`CREATE DATABASE blogg ...`) |
| 3 | JAR `postgresql-42.7.1.jar` dans `WEB-INF/lib` |
| 4 | `jdbc.user` / `jdbc.password` (et `jdbc.url` si besoin) dans `web.xml` |

Ensuite : lance l’application (Tomcat). Les tables et les données initiales (admin + premier article) sont créées au premier démarrage.

**Pourquoi PostgreSQL :** très stable, puissant, adapté aux projets qui grandissent et très bien supporté en cloud (AWS RDS, Azure, Google Cloud, etc.). Les données restent dans PostgreSQL même si tu quittes le projet ou changes de machine, tant que l’URL et les identifiants dans `web.xml` pointent vers la même base.
