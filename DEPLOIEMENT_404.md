# 404 sur http://localhost:8080/blogg/ – Que faire

Le message « La ressource demandée [/blogg/] n'est pas disponible » signifie en général que **l’application n’a pas démarré** (contexte non déployé ou erreur au démarrage).

---

## 1. Vérifier la console Eclipse (Tomcat)

Dans Eclipse, ouvre l’onglet **Console** (fenêtre en bas) quand tu lances Tomcat.

- S’il y a une **erreur rouge** au démarrage (par ex. `LifecycleException`, `RuntimeException`, `ClassNotFoundException`, erreur SQL), le contexte **blogg** ne démarre pas → d’où le 404.
- **À faire** : note le message d’erreur exact (ou copie-colle le) et corrige la cause (voir points suivants).

---

## 2. Mot de passe PostgreSQL dans web.xml

Si l’erreur parle de **connexion** ou **authentification** à la base :

1. Ouvre **`src/main/webapp/WEB-INF/web.xml`**.
2. Cherche **`jdbc.password`**.
3. Remplace **`METTRE_ICI_LE_MOT_DE_PASSE_POSTGRES`** par le **vrai mot de passe** de l’utilisateur PostgreSQL (souvent `postgres`), ou laisse **vide** si tu n’as pas de mot de passe :
   ```xml
   <param-value>ton_mot_de_passe</param-value>
   ```
   ou, si pas de mot de passe :
   ```xml
   <param-value></param-value>
   ```
4. **Enregistre** le fichier, **arrête** Tomcat, puis **relance** le serveur.

---

## 3. Vérifier que le projet est bien sur le serveur

1. Dans Eclipse, va dans l’onglet **Servers** (en bas).
2. Double-clic sur **Apache Tomcat 11…** (ou ton serveur).
3. Dans la page qui s’ouvre, regarde la section **« Projects »** (ou « Modules »).
4. **blogg** doit apparaître dans la liste des projets déployés (par ex. à droite, « Deployed projects »).
5. S’il n’y est pas : bouton **Add** / **Add and Remove…**, ajoute **blogg**, puis **Finish**. Enregistre, arrête et relance Tomcat.

---

## 4. Tester directement l’accueil

Une fois qu’il n’y a **plus d’erreur** au démarrage dans la Console :

- Ouvre : **http://localhost:8080/blogg/home**  
  (au lieu de seulement **http://localhost:8080/blogg/**).

Si **/blogg/home** fonctionne mais **/blogg/** reste en 404, le problème vient du **welcome file** (fichier d’accueil). On pourra alors ajouter une redirection depuis la racine.

---

## 5. Vérifier PostgreSQL

- Le **service PostgreSQL** doit être **démarré** (Services Windows ou pgAdmin).
- La base **blogg** doit **exister** (créée à la main ou par le script `sql/init_postgresql.sql`).

---

## Résumé

| À vérifier | Action |
|------------|--------|
| Console Eclipse | Lire l’erreur au démarrage de Tomcat et la corriger |
| web.xml | Renseigner le bon `jdbc.password` (ou le laisser vide) |
| Serveur | Vérifier que **blogg** est bien déployé sur Tomcat |
| URL de test | Essayer **http://localhost:8080/blogg/home** |
| PostgreSQL | Service démarré, base **blogg** créée |

En général, après avoir **corrigé l’erreur affichée dans la Console** (souvent le mot de passe PostgreSQL) et **relancé Tomcat**, **http://localhost:8080/blogg/** ou **http://localhost:8080/blogg/home** fonctionne.
