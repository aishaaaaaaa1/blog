# Publier le projet sur GitHub

## Avant de pousser : sécuriser les mots de passe

Dans **`src/main/webapp/WEB-INF/web.xml`**, remplace les **vrais mots de passe** par des placeholders pour ne pas les mettre sur GitHub :

- **`jdbc.password`** : mets `<param-value></param-value>` ou `<param-value>***</param-value>`
- **`mail.smtp.password`** : mets `<param-value>***</param-value>`

Après avoir cloné le projet, chaque personne remet ses vrais mots de passe en local.

---

## 1. Installer Git (si besoin)

- Télécharge : https://git-scm.com/download/win  
- Ou installe **GitHub Desktop** : https://desktop.github.com/

---

## 2. Créer un dépôt sur GitHub

1. Va sur https://github.com et connecte-toi.
2. Clique sur **« + »** (en haut à droite) → **« New repository »**.
3. **Repository name** : `blogg` (ou un autre nom).
4. Choisis **Public**, ne coche pas « Add a README » (le projet en a déjà).
5. Clique sur **« Create repository »**.
6. Note l’URL du dépôt, par ex. : `https://github.com/TON_USERNAME/blogg.git`

---

## 3. Publier depuis ton PC (ligne de commande)

Ouvre **PowerShell** ou **Invite de commandes** dans le dossier du projet (ou ouvre un terminal dans Cursor/Eclipse), puis exécute :

```bash
cd c:\Users\g\eclipse-workspace\blogg

git init
git add .
git commit -m "Initial commit - Blog Java JSP PostgreSQL"

git branch -M main
git remote add origin https://github.com/aishaaaaaaa1/blog.git
git push -u origin main
```

Ou **double-clique** sur **`pousser_sur_github.bat`** à la racine du projet (le script fait tout automatiquement). Si GitHub te demande de te connecter, utilise ton compte (ou un token).

---

## 4. Avec GitHub Desktop

1. Ouvre **GitHub Desktop**.
2. **File** → **Add local repository** → sélectionne le dossier `c:\Users\g\eclipse-workspace\blogg`.
3. Si Git n’est pas encore initialisé : clique sur **« Create a repository »** (ou fais d’abord `git init` en ligne de commande dans ce dossier).
4. Renseigne le **commit** (ex. « Initial commit ») et clique sur **« Commit to main »**.
5. **Publish repository** : choisis ton compte GitHub, le nom du dépôt (ex. `blogg`), puis **Publish**.

---

## 5. Après la première publication

- Le code sera sur **https://github.com/TON_USERNAME/blogg**.
- Pour les prochains changements : **commit** puis **push** (ou « Push origin » dans GitHub Desktop).

N’oublie pas d’avoir **remplacé les vrais mots de passe** par des placeholders dans `web.xml` avant le premier push.
