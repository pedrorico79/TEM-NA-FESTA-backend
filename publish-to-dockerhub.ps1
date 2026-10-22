#!/usr/bin/env pwsh
<#
.SYNOPSIS
    Script para build e push para Docker Hub (repositório público)

.DESCRIPTION
    Automatiza o processo de:
    1. Build da imagem Docker
    2. Login no Docker Hub
    3. Tag da imagem
    4. Push para Docker Hub

.PARAMETER DockerHubUsername
    Nome de usuário do Docker Hub (obrigatório)

.PARAMETER ImageName
    Nome da imagem (padrão: tem-na-festa)

.PARAMETER ImageTag
    Tag da imagem (padrão: latest)

.PARAMETER SkipBuild
    Se especificado, pula o step de build

.EXAMPLE
    .\publish-to-dockerhub.ps1 -DockerHubUsername "seu-usuario"

.EXAMPLE
    .\publish-to-dockerhub.ps1 -DockerHubUsername "seu-usuario" -ImageTag "v1.0.0"

.EXAMPLE
    .\publish-to-dockerhub.ps1 -DockerHubUsername "seu-usuario" -ImageName "meu-app" -ImageTag "latest"
#>

param(
    [Parameter(Mandatory=$true, HelpMessage="Docker Hub username")]
    [string]$DockerHubUsername,

    [Parameter(Mandatory=$false)]
    [string]$ImageName = "tem-na-festa",

    [Parameter(Mandatory=$false)]
    [string]$ImageTag = "latest",

    [Parameter(Mandatory=$false)]
    [switch]$SkipBuild,

    [Parameter(Mandatory=$false)]
    [switch]$SkipLogin
)

$ErrorActionPreference = "Stop"

# Cores
$colors = @{
    Success = "Green"
    Error   = "Red"
    Warning = "Yellow"
    Info    = "Cyan"
}

function Write-Log {
    param([string]$Message, [ValidateSet("Success", "Error", "Warning", "Info")][string]$Level = "Info")
    $prefix = @{ Success = "✅"; Error = "❌"; Warning = "⚠️"; Info = "ℹ️" }
    Write-Host "$($prefix[$Level]) $Message" -ForegroundColor $colors[$Level]
}

function Invoke-Safe {
    param([string]$Command, [string]$ErrorMsg)
    Write-Log "▶ $Command" -Level Info
    Invoke-Expression $Command
    if ($LASTEXITCODE -ne 0) {
        Write-Log $ErrorMsg -Level Error
        exit 1
    }
}

if (-not (Test-Path "Dockerfile")) {
    Write-Log "Dockerfile não encontrado!" -Level Error
    Write-Log "Execute no diretório raiz do projeto" -Level Info
    exit 1
}

$localImage = "${ImageName}:${ImageTag}"
$remoteImage = "${DockerHubUsername}/${localImage}"

Write-Host "`n🚀 Publicando para Docker Hub`n" -ForegroundColor Cyan
Write-Host "Usuário:  $DockerHubUsername" -ForegroundColor White
Write-Host "Imagem:   $localImage" -ForegroundColor White
Write-Host "Remoto:   $remoteImage" -ForegroundColor White
Write-Host ""

# STEP 1: Build
if (-not $SkipBuild) {
    Write-Log "STEP 1/4: Fazendo build..." -Level Info
    Invoke-Safe "docker build -t $localImage ." "Falha no build"
    Write-Log "Build concluído" -Level Success
    Write-Host ""
} else {
    Write-Log "STEP 1/4: Build pulado" -Level Warning
    Write-Host ""
}

# STEP 2: Login
if (-not $SkipLogin) {
    Write-Log "STEP 2/4: Login no Docker Hub..." -Level Info
    Write-Host "⚠️  Será solicitada sua senha do Docker Hub" -ForegroundColor Yellow
    Invoke-Safe "docker login -u $DockerHubUsername" "Falha no login"
    Write-Log "Login bem-sucedido" -Level Success
    Write-Host ""
} else {
    Write-Log "STEP 2/4: Login pulado" -Level Warning
    Write-Host ""
}

# STEP 3: Tag
Write-Log "STEP 3/4: Tagging imagem..." -Level Info
Invoke-Safe "docker tag $localImage $remoteImage" "Falha ao tagear"
Write-Log "Tagging concluído" -Level Success
Write-Host ""

# STEP 4: Push
Write-Log "STEP 4/4: Push para Docker Hub (pode levar alguns minutos)..." -Level Info
Invoke-Safe "docker push $remoteImage" "Falha no push"
Write-Log "Push concluído" -Level Success
Write-Host ""

# Resultado
Write-Host "✨ Sucesso! Imagem publicada!" -ForegroundColor Green
Write-Host ""
Write-Host "📊 Detalhes:" -ForegroundColor Cyan
Write-Host "  Docker Hub: https://hub.docker.com/r/$DockerHubUsername/$ImageName"
Write-Host "  URI:        docker.io/$remoteImage"
Write-Host "  Comando:    docker pull $remoteImage"
Write-Host ""
Write-Host "💡 Próximos passos:" -ForegroundColor Yellow
Write-Host "  1. Qualquer pessoa pode usar:"
Write-Host "     docker pull $remoteImage"
Write-Host "     docker run -d -p 8080:8080 -e DB_URL=... $remoteImage"
Write-Host ""
Write-Host "  2. Para usar no AWS ECR:"
Write-Host "     docker tag $remoteImage 123456789.dkr.ecr.us-east-1.amazonaws.com/tem-na-festa"
Write-Host "     docker push 123456789.dkr.ecr.us-east-1.amazonaws.com/tem-na-festa"
Write-Host ""

