# 📋 Exemplos de Requisições da API

Este documento contém exemplos práticos de todas as requisições disponíveis na API Mobiauto Backend.

## 🔑 Autenticação

### Login
```http
POST /auth/login
Content-Type: application/json

{
  "email": "usuario@exemplo.com",
  "password": "senha123"
}
```

**Resposta:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

---

## 👥 Usuários

### Cadastrar Usuário
```http
POST /users
Content-Type: application/json

{
  "firstName": "João",
  "lastName": "Silva",
  "email": "joao.silva@email.com",
  "password": "senha123"
}
```

### Buscar Usuário por ID
```http
GET /users/{id}
Authorization: Bearer {token}
```

### Atualizar Usuário
```http
PATCH /users/{id}
Content-Type: application/json
Authorization: Bearer {token}

{
  "firstName": "João Carlos",
  "lastName": "Silva Santos",
  "email": "joao.carlos@email.com",
  "password": "novaSenha123"
}
```

**Nota:** Todos os campos são opcionais na atualização.

### Desativar Usuário
```http
DELETE /users/{id}
Authorization: Bearer {token}
```

### Reativar Usuário
```http
PATCH /users/{id}/reactivate
Authorization: Bearer {token}
```

---

## 🏪 Lojas

### Cadastrar Loja
```http
POST /stores
Content-Type: application/json
Authorization: Bearer {token}

{
  "storeName": "AutoCenter Silva",
  "cnpj": "12345678000195"
}
```

### Listar Lojas Ativas
```http
GET /stores
Authorization: Bearer {token}
```

### Buscar Loja por ID
```http
GET /stores/{id}
Authorization: Bearer {token}
```

### Atualizar Loja
```http
PATCH /stores/{id}
Content-Type: application/json
Authorization: Bearer {token}

{
  "storeName": "AutoCenter Silva & Filhos",
  "cnpj": "12345678000195"
}
```

**Nota:** Todos os campos são opcionais na atualização.

### Desativar Loja
```http
DELETE /stores/{id}
Authorization: Bearer {token}
```

### Reativar Loja
```http
PATCH /stores/{id}/reactivate
Authorization: Bearer {token}
```

---

## 🚗 Ofertas

### Criar Oferta
```http
POST /stores/{storeId}/users/{userId}/offers
Content-Type: application/json
Authorization: Bearer {token}

{
  "type": "CAR",
  "model": "Honda Civic",
  "releaseYear": 2023,
  "color": "Preto"
}
```

**Tipos de veículo disponíveis:**
- `CAR` - Carro
- `MOTORCYCLE` - Motocicleta
- `TRUCK` - Caminhão

### Atualizar Oferta
```http
PATCH /offers/{id}
Content-Type: application/json
Authorization: Bearer {token}

{
  "type": "CAR",
  "model": "Honda Civic EX",
  "releaseYear": 2023,
  "color": "Branco"
}
```

**Nota:** Todos os campos são opcionais na atualização.

### Desativar Oferta
```http
DELETE /offers/{id}
Authorization: Bearer {token}
```

### Reativar Oferta
```http
PATCH /offers/{id}/reactivate
Authorization: Bearer {token}
```

---

## 📊 Monitoramento (Spring Boot Actuator)

### Health Check
```http
GET /actuator/health
```

**Resposta:**
```json
{
  "status": "UP"
}
```

---

## 📝 Estruturas de Resposta

### UserDto
```json
{
  "name": "João Silva",
  "email": "joao.silva@email.com"
}
```

### StoreDto
```json
{
  "name": "AutoCenter Silva",
  "cnpj": "12.345.678/0001-95",
  "totalOffers": 3,
  "offers": [
    {
      "status": "ACTIVE",
      "client": {
        "name": "João Silva",
        "email": "joao.silva@email.com"
      },
      "vehicle": {
        "model": "Honda Civic",
        "color": "Preto",
        "releaseYear": 2023,
        "type": "CAR"
      },
      "storeCnpj": "12.345.678/0001-95",
      "storeName": "AutoCenter Silva"
    }
  ]
}
```

### OfferDto
```json
{
  "status": "ACTIVE",
  "client": {
    "name": "João Silva",
    "email": "joao.silva@email.com"
  },
  "vehicle": {
    "model": "Honda Civic",
    "color": "Preto",
    "releaseYear": 2023,
    "type": "CAR"
  },
  "storeCnpj": "12.345.678/0001-95",
  "storeName": "AutoCenter Silva"
}
```

### VehicleDto
```json
{
  "model": "Honda Civic",
  "color": "Preto",
  "releaseYear": 2023,
  "type": "CAR"
}
```

---

## ❌ Tratamento de Erros

### Erro de Validação (400)
```json
{
  "message": "Campo obrigatório não informado",
  "status": 400,
  "error": "Validation Error"
}
```

### Erro de Autenticação (401)
```json
{
  "message": "Invalid credentials",
  "status": 401,
  "error": "Unauthorized"
}
```

### Recurso Não Encontrado (404)
```json
{
  "message": "User with id 999 not found",
  "status": 404,
  "error": "Not Found"
}
```

### Conflito de Dados (409)
```json
{
  "message": "Email already exists",
  "status": 409,
  "error": "Conflict"
}
```

### Erro Interno (500)
```json
{
  "message": "An unexpected error occurred",
  "status": 500,
  "error": "Internal Server Error"
}
```

---

## 🔧 Configuração de Headers

Para todas as requisições autenticadas, é necessário incluir o header:

```
Authorization: Bearer {seu_token_jwt}
Content-Type: application/json
```

O token JWT é obtido através do endpoint de login (`POST /auth/login`).