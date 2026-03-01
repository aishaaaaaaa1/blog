# Corriger pgAdmin 4 : "No module named 'pgadmin'"

## Script automatique (recommandé)

Double-clique sur **`docs/reparer_et_lancer_pgAdmin4.bat`** : le script ferme pgAdmin, supprime le profil, puis relance pgAdmin depuis le bon répertoire. Si tu as PostgreSQL 17 (pas 18), édite le .bat et remplace `18` par `17` dans les chemins.

---

## Solution 1 : Supprimer le profil pgAdmin à la main

1. **Ferme pgAdmin** complètement (y compris depuis la barre des tâches).
2. **Supprime le dossier de configuration** :
   - Ouvre l’Explorateur de fichiers.
   - Colle dans la barre d’adresse :  
     `%APPDATA%\pgadmin4`
   - Supprime tout le contenu du dossier **ou** le dossier `pgadmin4` entier.
3. **Relance pgAdmin 4** depuis le menu Démarrer (PostgreSQL 18 > pgAdmin 4).

Au premier lancement, pgAdmin te redemandera de définir un mot de passe maître (pour le coffre des mots de passe).

---

## Solution 2 : Lancer pgAdmin depuis le bon répertoire

Si la solution 1 ne change rien, le lanceur ne part peut‑être pas du bon répertoire.

1. Ouvre **PowerShell** ou **Invite de commandes** en tant qu’utilisateur.
2. Exécute :

```powershell
cd "C:\Program Files\PostgreSQL\18\pgAdmin 4\web"
& "C:\Program Files\PostgreSQL\18\pgAdmin 4\python\python.exe" -s "C:\Program Files\PostgreSQL\18\pgAdmin 4\web\pgAdmin4.py"
```

Si pgAdmin démarre ainsi, le problème vient du raccourci. Tu peux créer un nouveau raccourci qui lance cette commande avec le répertoire de travail :  
`C:\Program Files\PostgreSQL\18\pgAdmin 4\web`.

---

## Solution 3 : Réparer l’installation PostgreSQL

1. **Panneau de configuration** > **Programmes et fonctionnalités**.
2. Choisis **PostgreSQL 18** > **Modifier**.
3. **Réparer** l’installation.
4. Redémarre le PC puis relance pgAdmin 4.

---

## Solution 4 : Utiliser un autre client pour PostgreSQL

En attendant que pgAdmin fonctionne, tu peux gérer la base **blogg** avec :

- **psql** (ligne de commande, fourni avec PostgreSQL 18)  
  Menu Démarrer > PostgreSQL 18 > SQL Shell (psql), puis connexion et `\c blogg`.
- **DBeaver** (gratuit) : https://dbeaver.io/download/
- **HeidiSQL** (gratuit) : https://www.heidisql.com/download.php

Tu peux exécuter le script `sql/init_postgresql.sql` (création de la base + tables) depuis n’importe quel client connecté à PostgreSQL.
