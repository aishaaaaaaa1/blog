# Pourquoi je ne reçois aucun email ?

**Par défaut, aucun serveur SMTP n’est configuré.** L’application n’envoie donc aucun email tant que vous n’avez pas renseigné les paramètres de votre boîte mail.

---

## Activer l’envoi d’emails (obligatoire pour recevoir le lien)

### 1. Ouvrir la configuration

Ouvrez le fichier **`src/main/webapp/WEB-INF/web.xml`** (ou **`WEB-INF/web.xml`** dans le projet déployé).

### 2. Décommenter et remplir les paramètres SMTP

Repérez le bloc commenté qui commence par `<!-- Configuration SMTP` et **supprimez les marques de commentaire** `<!--` et `-->` autour des lignes, puis **remplacez les valeurs** par les vôtres.

Exemple après modification (avec Gmail) :

```xml
<context-param><param-name>mail.smtp.host</param-name><param-value>smtp.gmail.com</param-value></context-param>
<context-param><param-name>mail.smtp.port</param-name><param-value>587</param-value></context-param>
<context-param><param-name>mail.smtp.user</param-name><param-value>votre.email@gmail.com</param-value></context-param>
<context-param><param-name>mail.smtp.password</param-name><param-value>xxxx xxxx xxxx xxxx</param-value></context-param>
<context-param><param-name>mail.from</param-name><param-value>votre.email@gmail.com</param-value></context-param>
<context-param><param-name>mail.from.name</param-name><param-value>Blogg</param-value></context-param>
```

### 3. Gmail : mot de passe d’application

Avec **Gmail**, le mot de passe normal ne fonctionne pas. Il faut utiliser un **mot de passe d’application** :

1. Allez sur [https://myaccount.google.com/security](https://myaccount.google.com/security).
2. Activez la **validation en 2 étapes** si ce n’est pas déjà fait.
3. Dans « Connexion à Google », créez un **Mot de passe d’application** (pour « Mail »).
4. Utilisez ce mot de passe (16 caractères) dans `mail.smtp.password` dans `web.xml`.

### 4. Autres fournisseurs (exemples)

| Fournisseur | Host SMTP        | Port |
|-------------|------------------|------|
| Gmail       | smtp.gmail.com   | 587  |
| Outlook     | smtp-mail.outlook.com | 587 |
| Yahoo       | smtp.mail.yahoo.com   | 587 |
| Orange      | smtp.orange.fr       | 587 |

Renseignez `mail.smtp.user` et `mail.smtp.password` avec l’adresse et le mot de passe (ou mot de passe d’application) du compte qui envoie les mails.

### 5. Redémarrer l’application

Après avoir modifié `web.xml`, **redémarrez** le serveur Tomcat (ou republiez le projet) pour que la nouvelle configuration soit prise en compte.

---

**Résumé :** tant que le bloc SMTP dans `web.xml` est commenté ou que les valeurs sont vides / incorrectes, **aucun email n’est envoyé**. Une fois la configuration correcte et le serveur redémarré, les inscriptions déclencheront l’envoi du lien de validation par email.
