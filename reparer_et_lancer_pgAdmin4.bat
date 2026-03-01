@echo off
chcp 65001 >nul
echo ========================================
echo   Réparer et lancer pgAdmin 4
echo ========================================
echo.

echo [1/3] Fermeture de pgAdmin 4 et processus associes...
taskkill /F /IM pgAdmin4.exe 2>nul
taskkill /F /IM "python.exe" /FI "WINDOWTITLE eq pgAdmin*" 2>nul
timeout /t 2 /nobreak >nul
echo.

echo [2/3] Suppression du profil pgAdmin (pour corriger "No module named pgadmin")...
if exist "%APPDATA%\pgadmin4" (
    rd /s /q "%APPDATA%\pgadmin4" 2>nul
    if exist "%APPDATA%\pgadmin4" (
        echo    ATTENTION : Certains fichiers sont encore verrouilles.
        echo    Ferme pgAdmin manuellement (Barre des taches, Gestionnaire des taches),
        echo    puis relance ce script.
        pause
        exit /b 1
    ) else (
        echo    Profil supprime.
    )
) else (
    echo    Dossier pgadmin4 absent ou deja supprime.
)
echo.

echo [3/3] Lancement de pgAdmin 4 (PYTHONPATH = dossier web)...
set "PGADMIN_WEB=C:\Program Files\PostgreSQL\18\pgAdmin 4\web"
set "PYTHONPATH=%PGADMIN_WEB%"
cd /d "%PGADMIN_WEB%"
start "" "C:\Program Files\PostgreSQL\18\pgAdmin 4\python\python.exe" -s "%PGADMIN_WEB%\pgAdmin4.py"

echo.
echo pgAdmin 4 a ete lance. Au premier demarrage, definis un mot de passe maitre.
echo.
pause
