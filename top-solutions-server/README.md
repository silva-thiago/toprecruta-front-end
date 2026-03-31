# Top Solutions – Back-end

API REST de gerenciamento de usuários construída com **Spring Boot 4.0.5** e **Java 25**.

## Stack

| Camada | Tecnologia |
|---|---|
| Linguagem | Java 25 LTS |
| Framework | Spring Boot 4.0.5 |
| Segurança | Spring Security 7 + JJWT 0.12.6 |
| Persistência | Spring Data JPA + Hibernate |
| Banco | PostgreSQL (H2 nos testes) |
| Build | Maven (Jar) |

## Endpoints

### Auth

| Método | Rota | Acesso |
|---|---|---|
| POST | `/api/auth/login` | Público |

#### Payload de login
```json
{
  "email": "admin@topsolutions.com",
  "password": "Admin@1234"
}
```

#### Resposta
```json
{
  "token": "eyJhbGci...",
  "type": "Bearer",
  "user": { ... }
}
```

### Usuários

| Método | Rota | Acesso |
|---|---|---|
| GET | `/api/users` | Autenticado |
| GET | `/api/users/{id}` | Autenticado |
| POST | `/api/users` | ADMIN |
| PUT | `/api/users/{id}` | ADMIN |
| DELETE | `/api/users/{id}` | ADMIN |

#### Parâmetros de listagem (`GET /api/users`)

| Param | Tipo | Padrão | Descrição |
|---|---|---|---|
| `search` | string | — | Filtra por nome ou e-mail |
| `active` | boolean | — | Filtra por status |
| `page` | int | 0 | Página (zero-based) |
| `size` | int | 10 | Itens por página |
| `sort` | string | `name` | Campo de ordenação (prefixe com `-` para DESC) |

## Configuração

### 1. PostgreSQL

Crie o banco antes de subir:

```sql
CREATE DATABASE topsolutions;
```

### 2. Variáveis de ambiente (opcional)

| Variável | Padrão | Descrição |
|---|---|---|
| `DB_USERNAME` | `postgres` | Usuário do banco |
| `DB_PASSWORD` | `postgres` | Senha do banco |
| `DB_NAME` | `topsolutions` | Nome do banco |
| `JWT_SECRET` | valor embutido | Chave secreta JWT |
| `JWT_EXPIRATION_MS` | `1500000` | Expiração em ms (15min) |
| `CORS_ORIGINS` | `http://localhost:5173` | Origins permitidas |

### 3. Rodar

```bash
./mvnw spring-boot:run
```

Em produção, **sempre** defina `JWT_SECRET` com uma chave gerada:
```bash
openssl rand -base64 64
```

## Usuário inicial

Ao subir pela primeira vez, um usuário admin é criado automaticamente:

- **E-mail:** `admin@topsolutions.com`
- **Senha:** `Admin@1234`

> Altere a senha após o primeiro acesso.

## Estrutura de pacotes

```
br.com.topsolutions
├── config/          # SecurityConfig, DataInitializer
├── controller/      # AuthController, UserController
├── dto/
│   ├── request/     # LoginRequest, CreateUserRequest, UpdateUserRequest
│   └── response/    # AuthResponse, UserResponse, PageResponse, ApiError
├── entity/          # User
├── exception/       # GlobalExceptionHandler, ResourceNotFoundException, BusinessException
├── filter/          # JwtAuthFilter
├── repository/      # UserRepository
├── security/        # JwtService, UserDetailsServiceImpl
└── service/
    └── impl/        # AuthServiceImpl, UserServiceImpl
```
