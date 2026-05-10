<div align="center">

# KernelPanic — Backend

**Projeto Integrador 3º Semestre · DSM · FATEC São José dos Campos**  
Cliente: **GSW Soluções Integradas**

**Java 21 · Spring Boot 4 · Spring Cloud Gateway · MySQL · Docker · JWT**

Repositório principal do projeto: **[Kernel-Panic-FatecSjc/KernelPanic-3DSM-API](https://github.com/Kernel-Panic-FatecSjc/KernelPanic-3DSM-API)**

</div>

---

## Sobre

Backend do sistema de **controle de horas e apontamentos** da GSW Soluções Integradas. Arquitetura de microsserviços com um API Gateway centralizado que roteia requisições, aplica autenticação JWT e gerencia CORS.

---

## Arquitetura

```
api-gateway  (porta 8080)
├── auth-service          → /auth/**       (porta 8081) — login e geração de token
├── usuario-service       → /usuario/**    (porta 8083) — cadastro e gestão de usuários
├── projeto-service       → /projeto/**    (porta 8082) — projetos e participações
├── apontamentohoras-service → /horas/**  (porta 8084) — registro e aprovação de horas
└── task-service                          — gestão de tarefas
```

Cada serviço é um submódulo Git independente. O gateway valida o token JWT em todas as rotas protegidas antes de encaminhar a requisição.

---

## Tecnologias

| Tecnologia | Versão | Uso |
|---|---|---|
| Java | 21 | Linguagem principal |
| Spring Boot | 4.0.4 | Framework base |
| Spring Cloud Gateway | 2025.1.1 | Roteamento e filtros |
| Spring Cloud Netflix Eureka | 2025.1.1 | Service discovery |
| JWT (jjwt) | 0.12.5 | Autenticação |
| MySQL | 8.x | Banco de dados relacional |
| Docker | — | Containerização dos serviços |
| Lombok | — | Redução de boilerplate |
| Maven | — | Gerenciamento de dependências |

---

## Pré-requisitos

- **Java 21** ou superior
- **Maven** 3.9 ou superior
- **MySQL** 8 em execução
- **Docker** (opcional, para rodar via container)

---

## Banco de dados

Crie o banco antes de subir os serviços:

```powershell
mysql -u root -p < api-gateway\bancoGeral.sql
```

Isso cria o banco `bancogeral` com as tabelas: `usuarios`, `projetos`, `participacao`, e demais tabelas do sistema.

---

## Como executar

Cada microsserviço possui seu próprio `docker-compose.yml. Suba cada um individualmente:

```powershell
# auth-service (porta 8081 · banco: bancoauth · MySQL: 3307)
cd auth-service
docker compose up --build -d

# usuario-service (porta 8083 · banco: kernel_panic_db · MySQL: 3306)
cd ..\usuario-service
docker compose up --build -d

# projeto-service (porta 8082 · banco: db_kernelpanic · MySQL: 3038)
cd ..\projeto-service
docker compose up --build -d

# apontamentohoras-service (porta 8084)
cd ..\apontamentohoras-service
docker compose up --build -d

# task-service
cd ..\task-service
docker compose up --build -d
```

 Cada serviço sobe seu próprio container MySQL. Verifique se as portas não estão em conflito antes de subir todos juntos.

---

## Variáveis de configuração

Configure em `application.yml` de cada serviço conforme necessário:

```yaml
# api-gateway/src/main/resources/application.yml
jwt:
  secret: <sua_chave_secreta_com_mais_de_256_bits>

server:
  port: 8080
```

 **Atenção:** nunca versione a chave JWT real. Use variáveis de ambiente em produção.

---

## Submódulos

Este repositório usa Git Submodules. Para atualizar todos:

```powershell
git submodule update --init --recursive
git submodule update --remote
```

| Submódulo | Repositório |
|---|---|
| auth-service | KP-3DSM-API-auth-service |
| usuario-service | KP-3DSM-API-usuario-service |
| projeto-service | KP-3DSM-API-projeto-service |
| apontamentohoras-service | KP-3DSM-API-apontamentohoras-service |
| task-service | KP-3DSM-API-task-service |

---

## Rotas do Gateway

| Rota | Serviço | Autenticação |
|---|---|---|
| `/auth/**` | auth-service | Pública |
| `/usuario/**` | usuario-service | Protegida |
| `/projeto/**` | projeto-service | Protegida |
| `/horas/**` | apontamentohoras-service | Protegida |

