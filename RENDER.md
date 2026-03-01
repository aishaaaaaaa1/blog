# Déployer Blogg sur Render avec Docker

## 1. Sur Render : type de service

- **Environment** : choisir **Docker** (pas "Node").
- **Root Directory** : laisser **vide** (le dépôt est à la racine).
- **Build Command** et **Start Command** : les **laisser vides** quand vous utilisez Docker. Render build et démarre l’image avec le `Dockerfile`.

## 2. Base de données PostgreSQL sur Render

1. Dans le dashboard Render, créez un **PostgreSQL** (Add → PostgreSQL).
2. Notez l’**Internal Database URL** (ou Host, Database, User, Password).
3. Dans les **Environment Variables** de votre service Web (Docker), ajoutez par exemple :
   - `JDBC_URL` = l’URL fournie par Render (souvent du type `postgresql://...` ; si besoin, adaptez en `jdbc:postgresql://host:port/database`).
   - `JDBC_USER` = utilisateur de la base.
   - `JDBC_PASSWORD` = mot de passe.

L’app lit aujourd’hui la config dans `web.xml`. Pour que Render utilise ces variables, il faudra soit modifier le code pour lire `JDBC_URL`, `JDBC_USER`, `JDBC_PASSWORD` depuis les variables d’environnement (et les utiliser dans `DbInitListener` / `DbHelper`), soit générer un `web.xml` au build à partir de ces variables. Pour un premier déploiement, vous pouvez temporairement mettre l’URL Render dans `web.xml` (sans committer de mot de passe réel).

## 3. Build en local (optionnel)

```bash
mvn clean package
docker build -t blogg .
docker run -p 8080:8080 -e PORT=8080 blogg
```

Puis ouvrir http://localhost:8080 .

## 4. Résumé des champs Render

| Champ              | Valeur pour ce projet      |
|-------------------|----------------------------|
| Environment       | **Docker**                 |
| Root Directory    | *(vide)*                   |
| Build Command     | *(vide)*                   |
| Start Command     | *(vide)*                   |

Le `Dockerfile` fait le build Maven et lance Tomcat ; le port est pris depuis la variable `PORT` fournie par Render.
