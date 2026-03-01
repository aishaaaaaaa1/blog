# Blogg

Blog professionnel réalisé avec **Servlets**, **JSP** et **JSTL** (objectif pédagogique).

## Outils

- **JDK 21**
- **Eclipse** (Dynamic Web Project)
- **Apache Tomcat 11**
- Navigateurs : Mozilla Firefox, Google Chrome

## Fonctionnalités

1. **Espace membres** : inscription, connexion, déconnexion
2. **Gestion des articles** : création, modification, suppression, liste et détail
3. **Gestion des commentaires** : ajout et suppression sur chaque article
4. **Gestion des profils** : consultation et édition (bio, nom d'affichage)
5. **Validation par email** : le lien d’activation est envoyé par email (jamais affiché sur la page)
6. **Internationalisation** : français et anglais (sélecteur de langue dans l’en-tête)

## Structure du projet

```
src/main/java/fr/blogg/
├── model/       (Member, Article, Comment, Profile)
├── dao/         (MemberDao, ArticleDao – simulation en mémoire)
├── filter/      (EncodingFilter, LocaleFilter, AuthFilter)
├── service/    (EmailService – envoi du lien de validation)
└── servlet/     (Home, Login, Register, ValidateEmail, Logout, Language,
                  Article, Comment, Profile)
src/main/webapp/
├── css/style.css
├── WEB-INF/
│   ├── web.xml
│   ├── jsp/     (vues JSP + JSTL)
│   └── ...
src/main/resources/
├── messages_fr.properties
└── messages_en.properties
```

## Démarrage

1. Importer le projet dans Eclipse (Existing Project).
2. **JSTL (pour supprimer les erreurs dans les JSP sous Eclipse)** :  
   - Clic droit sur le projet → Build Path → Add Libraries → **JSTL 1.2** si disponible, **ou**  
   - Télécharger la JSTL Jakarta (pour Tomcat 10+) et placer les JAR dans `WEB-INF/lib` :  
     - `jakarta.servlet.jsp.jstl-api-3.0.0.jar`  
     - `jakarta.servlet.jsp.jstl-3.0.1.jar` (implémentation, ex. Glassfish)  
   Les JSP utilisent les URI classiques `http://java.sun.com/jsp/jstl/core` et `.../fmt`. Sans ces bibliothèques, Eclipse peut afficher des erreurs sur les balises JSTL alors que l’application tourne correctement sur Tomcat.
3. Configurer le serveur Tomcat et lancer l’application sur le projet.
4. Accéder à l’application via l’URL du serveur (ex. `http://localhost:8080/blogg/`).

**Envoi du lien de validation par email** : décommenter et renseigner les paramètres SMTP dans `WEB-INF/web.xml` (mail.smtp.host, mail.smtp.port, mail.smtp.user, mail.smtp.password, mail.from). Sans SMTP, aucun email n'est envoyé.

## Compte de démonstration

- **Email :** `admin@blogg.fr`  
- **Mot de passe :** `admin`  

(Compte déjà validé, utilisable pour tester la connexion et la gestion des articles/commentaires.)

## Internationalisation

Le sélecteur de langue (LANGUES : Français / English) est affiché dans l’en-tête. La langue choisie est conservée en session et appliquée à toutes les libellés (JSTL `<fmt:message>`).
