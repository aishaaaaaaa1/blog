@echo off
chcp 65001 >nul
REM Utilise Git depuis son dossier d'installation (au cas ou il n'est pas dans le PATH)
set "GIT_EXE=C:\Program Files\Git\bin\git.exe"
if not exist "%GIT_EXE%" (
    echo Git introuvable dans "C:\Program Files\Git\bin\"
    echo Installe Git : https://git-scm.com/download/win
    pause
    exit /b 1
)
set "PATH=%PATH%;C:\Program Files\Git\bin"

echo ========================================
echo   Publication sur GitHub (aishaaaaaaa1/blog)
echo ========================================
echo.

cd /d "c:\Users\g\eclipse-workspace\blogg"

echo [1/5] git init...
"%GIT_EXE%" init

echo.
echo [2/5] git add . ...
"%GIT_EXE%" add .

echo.
echo [3/5] git commit...
"%GIT_EXE%" commit -m "Initial commit - Blog Java JSP PostgreSQL"
if errorlevel 1 (
    echo Info : rien a committer ou commit deja fait.
)

echo.
echo [4/5] Branche main + remote origin...
"%GIT_EXE%" branch -M main
"%GIT_EXE%" remote remove origin 2>nul
"%GIT_EXE%" remote add origin https://github.com/aishaaaaaaa1/blog.git

echo.
echo [5/5] git push -u origin main...
"%GIT_EXE%" push -u origin main

if errorlevel 1 (
    echo.
    echo Si erreur d'authentification : GitHub va peut-etre ouvrir le navigateur pour te connecter.
    echo Ou utilise un token : https://github.com/settings/tokens
)
echo.
pause
