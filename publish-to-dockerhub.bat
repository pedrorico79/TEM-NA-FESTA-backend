@echo off
REM Script para publicar no Docker Hub (Windows)
REM Uso: publish-to-dockerhub.bat seu-usuario [tag]

setlocal enabledelayedexpansion

if "%1"=="" (
    echo Uso: %0 seu-usuario [tag]
    echo.
    echo Exemplo:
    echo   %0 seu-usuario                  (tag: latest)
    echo   %0 seu-usuario v1.0.0           (tag: v1.0.0)
    echo.
    exit /b 1
)

set DOCKER_USER=%1
set IMAGE_TAG=%2
if "%2"=="" set IMAGE_TAG=latest

set LOCAL_IMAGE=tem-na-festa:%IMAGE_TAG%
set REMOTE_IMAGE=%DOCKER_USER%/tem-na-festa:%IMAGE_TAG%

echo.
echo ===================================================
echo   Docker Hub Publisher
echo ===================================================
echo.
echo Usuário:  %DOCKER_USER%
echo Imagem:   %LOCAL_IMAGE%
echo Remoto:   %REMOTE_IMAGE%
echo.
echo Começando processo de publicação...
echo.

REM Step 1: Build
echo [1/4] Fazendo build da imagem...
docker build -t %LOCAL_IMAGE% .
if errorlevel 1 (
    echo [ERRO] Falha no build!
    exit /b 1
)
echo [OK] Build concluído!
echo.

REM Step 2: Login
echo [2/4] Login no Docker Hub...
docker login -u %DOCKER_USER%
if errorlevel 1 (
    echo [ERRO] Falha no login!
    exit /b 1
)
echo [OK] Login bem-sucedido!
echo.

REM Step 3: Tag
echo [3/4] Tagging da imagem...
docker tag %LOCAL_IMAGE% %REMOTE_IMAGE%
if errorlevel 1 (
    echo [ERRO] Falha ao tagear!
    exit /b 1
)
echo [OK] Tagging concluído!
echo.

REM Step 4: Push
echo [4/4] Push para Docker Hub (pode levar alguns minutos)...
docker push %REMOTE_IMAGE%
if errorlevel 1 (
    echo [ERRO] Falha no push!
    exit /b 1
)
echo [OK] Push concluído!
echo.

echo ===================================================
echo   Sucesso! Imagem publicada!
echo ===================================================
echo.
echo Docker Hub: https://hub.docker.com/r/%DOCKER_USER%/tem-na-festa
echo URI:        docker.io/%REMOTE_IMAGE%
echo Comando:    docker pull %REMOTE_IMAGE%
echo.
echo Qualquer pessoa pode usar:
echo   docker run -d -p 8080:8080 -e DB_URL=... %REMOTE_IMAGE%
echo.

