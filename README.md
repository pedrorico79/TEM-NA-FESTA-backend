# TEM NA FESTA - Backend

Backend do sistema Tem Na Festa, responsável pela gestão de clientes, eventos, produtos, pedidos, pagamentos, usuários, lembretes e relatórios da operação.

---

# Visão Geral

Este projeto é uma API REST desenvolvida em Java com Spring Boot, focada no backoffice e na gestão da operação da confeitaria.

O backend oferece:

- Autenticação via JWT
- Persistência em MySQL
- Documentação interativa com Swagger/OpenAPI
- Integração com RabbitMQ para processamento assíncrono

---

# Tecnologias

- Java 21
- Spring Boot 3.5.13
- Spring Web
- Spring Data JPA
- Spring Security
- JWT (JSON Web Token)
- MySQL
- RabbitMQ
- Swagger / OpenAPI
- Maven
- Docker
- Docker Compose

---

# Funcionalidades

- Autenticação e autorização de usuários
- Gestão de clientes
- Gestão de eventos
- Gestão de produtos
- Gestão de pedidos
- Controle de pagamentos
- Registro e consulta de métodos de pagamento
- Lembretes e acompanhamento de pedidos
- Relatórios e KPIs
- Status de produção e fluxo operacional
- Health Check da aplicação
- Documentação interativa da API via Swagger UI

---

# Estrutura do Projeto

```text
TEM-NA-FESTA-backend/
├── .env.example
├── .dockerignore
├── .gitignore
├── Dockerfile
├── docker-compose.yml
├── HELP.md
├── pom.xml
├── mvnw
├── mvnw.cmd
├── settings.xml
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/temnafesta/
│   │   │       ├── application/
│   │   │       ├── domain/
│   │   │       ├── infrastructure/
│   │   │       └── presentation/
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── application-prod.properties
│   │       └── db/
│   └── test/
│       └── java/
├── README.md
└── target/
```

---

# Pré-requisitos

Antes de iniciar o projeto, certifique-se de ter instalado:

- JDK 21+
- Maven 3.9+
- MySQL 8+
- RabbitMQ
- Docker e Docker Compose (opcional)
- Git

---

# Configuração de Ambiente

A aplicação utiliza variáveis de ambiente para configurar MySQL, JWT e porta da aplicação.

Você pode usar o arquivo `.env.example` como referência ou configurar as variáveis diretamente no ambiente da máquina.

## Exemplo

```env
DB_URL=jdbc:mysql://localhost:3306/tem_na_festa?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC
DB_USER=root
DB_PASSWORD=sua_senha
JWT_SECRET=sua_chave_secreta_muito_segura
PORT=8080
```

## Observações importantes

- A chave JWT deve ter pelo menos 32 caracteres.
- O banco deve existir antes da aplicação iniciar.
- O projeto carrega variáveis de ambiente com o pacote `dotenv-java` quando disponível.
- O projeto também aceita configurações via variáveis de ambiente do sistema operacional.

---

# Executando Localmente

## 1. Clone o repositório

```bash
git clone https://github.com/pedrorico79/TEM-NA-FESTA-backend.git
cd TEM-NA-FESTA-backend
```

## 2. Configure as variáveis de ambiente

### Linux/macOS

```bash
export DB_URL="jdbc:mysql://localhost:3306/tem_na_festa?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC"
export DB_USER="root"
export DB_PASSWORD="sua_senha"
export JWT_SECRET="sua_chave_secreta"
export PORT=8080
```

### Windows (PowerShell)

```powershell
$env:DB_URL="jdbc:mysql://localhost:3306/tem_na_festa?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC"
$env:DB_USER="root"
$env:DB_PASSWORD="sua_senha"
$env:JWT_SECRET="sua_chave_secreta"
$env:PORT="8080"
```

## 3. Execute a aplicação

### Linux/macOS

```bash
./mvnw clean install
./mvnw spring-boot:run
```

### Windows

```powershell
mvnw.cmd clean install
mvnw.cmd spring-boot:run
```

A aplicação ficará disponível em:

```text
http://localhost:8080
```

---

# Executando com Docker

O projeto inclui um `docker-compose.yml` para subir a aplicação em container.

```bash
docker compose up --build
```

Ou, em versões antigas do Docker:

```bash
docker-compose up --build
```

---

# Documentação da API

A documentação Swagger/OpenAPI está disponível em:

```text
http://localhost:8080/swagger-ui/index.html
```

Também há um endpoint de Health Check:

```text
http://localhost:8080/api/v1/health
```

---

# Principais Endpoints

Alguns dos módulos disponibilizados pela API:

```text
/api/v1/auth
/api/v1/usuarios
/api/v1/clientes
/api/v1/eventos
/api/v1/produtos
/api/v1/pedidos
/api/v1/pedidos/{pedidoId}/pagamentos
/api/v1/metodos-pagamento
/api/v1/lembretes
/api/v1/relatorios
/api/v1/status
/api/v1/health
```

Consulte a documentação Swagger para detalhes completos de cada rota, payload e respostas.

---

# Segurança

A API utiliza Spring Security com autenticação baseada em JWT.

Os endpoints protegidos exigem um token válido no header:

```http
Authorization: Bearer <token>
```

Além disso, a aplicação permite acesso público apenas para:

- Autenticação
- Health Check

---

# Variáveis de Ambiente do Projeto

| Variável | Descrição |
|-----------|------------|
| DB_URL | URL de conexão com o MySQL |
| DB_USER | Usuário do banco |
| DB_PASSWORD | Senha do banco |
| JWT_SECRET | Chave secreta para geração dos tokens JWT |
| PORT | Porta da aplicação |
| JPA_DDL_AUTO | Estratégia de atualização do schema |
| JPA_SHOW_SQL | Habilitação de logs SQL |

---

# Testes

Para executar os testes do projeto:

```bash
./mvnw test
```

---

# Arquitetura

Este backend segue uma arquitetura organizada em camadas:

- `application`
- `domain`
- `infrastructure`
- `presentation`

Essa organização facilita:

- Manutenção
- Testabilidade
- Baixo acoplamento
- Evolução da aplicação
- Separação de responsabilidades

---

# Licença

Este projeto é destinado ao uso acadêmico e de extensão.

Consulte o responsável pelo repositório para informações específicas sobre uso e distribuição.
