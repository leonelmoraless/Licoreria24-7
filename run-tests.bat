@echo off
echo ========================================
echo Ejecutando Pruebas de Licoreria24-7
echo ========================================
echo.

echo [1/3] Limpiando proyecto...
call mvnw.cmd clean

echo.
echo [2/3] Compilando proyecto...
call mvnw.cmd compile test-compile

echo.
echo [3/3] Ejecutando todas las pruebas...
call mvnw.cmd test

echo.
echo ========================================
echo Pruebas completadas
echo ========================================
pause
