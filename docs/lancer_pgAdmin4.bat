@echo off
REM Lance pgAdmin 4 avec PYTHONPATH pour eviter "No module named 'pgadmin'"
set "PGADMIN_WEB=C:\Program Files\PostgreSQL\18\pgAdmin 4\web"
set "PYTHONPATH=%PGADMIN_WEB%"
cd /d "%PGADMIN_WEB%"
"C:\Program Files\PostgreSQL\18\pgAdmin 4\python\python.exe" -s "%PGADMIN_WEB%\pgAdmin4.py"
pause
