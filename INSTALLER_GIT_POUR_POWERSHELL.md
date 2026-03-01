# Utiliser Git dans PowerShell

Git est installé dans **`C:\Program Files\Git\bin\`** mais n'est pas dans le **PATH** de PowerShell, donc la commande `git` n'est pas reconnue.

## Solution 1 : Double-cliquer sur le script (le plus simple)

Le fichier **`pousser_sur_github.bat`** utilise directement le chemin complet vers Git.  
Ouvre l’Explorateur de fichiers → dossier **blogg** → **double-clic** sur **`pousser_sur_github.bat`**.  
Pas besoin d’ajouter Git au PATH.

---

## Solution 2 : Ajouter Git au PATH (pour que `git` marche dans PowerShell)

1. Ouvre **Paramètres Windows** (Windows + I).
2. **Système** → **À propos** → **Paramètres système avancés** (ou cherche « variables d’environnement »).
3. **Variables d’environnement**.
4. Dans **Variables utilisateur**, sélectionne **Path** → **Modifier**.
5. **Nouveau** → ajoute :  
   `C:\Program Files\Git\bin`
6. **OK** partout.
7. **Ferme puis rouvre PowerShell** (ou redémarre le PC).

Ensuite, dans PowerShell :

```powershell
cd c:\Users\g\eclipse-workspace\blogg
git init
git add .
git commit -m "Initial commit - Blog Java JSP PostgreSQL"
git branch -M main
git remote add origin https://github.com/aishaaaaaaa1/blog.git
git push -u origin main
```

---

## Solution 3 : Utiliser GitHub Desktop

- Télécharge : https://desktop.github.com/
- Ouvre GitHub Desktop → **File** → **Add local repository** → sélectionne `c:\Users\g\eclipse-workspace\blogg`.
- Si le dossier n’est pas encore un dépôt Git : **Create a repository**.
- Fais ton commit puis **Publish repository** vers **aishaaaaaaa1/blog**.
