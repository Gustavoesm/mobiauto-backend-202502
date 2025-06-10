# Mobiauto Backend

Projeto desenvolvido por [@Gustavoesm](https://github.com/Gustavoesm) (Gustavo Eugênio) para atender ao desafio tecnico de backend com Java/Spring proposto pela Mobiauto.

## 📋 Descrição

O **Mobiauto Backend** é uma API REST desenvolvida em Spring Boot para gerenciamento de ofertas de veículos. O sistema permite que lojas cadastrem veículos e clientes façam ofertas, proporcionando uma plataforma completa para transações automotivas.

## 🚀 Tecnologias Utilizadas

- **Java 17**
- **Spring Boot 3.5.0**
- **Spring Security** (Autenticação JWT)
- **Spring Data JPA** (Persistência de dados)
- **PostgreSQL** (Banco de dados principal)
- **H2** (Banco de dados para testes)
- **Docker & Docker Compose** (Containerização)
- **Maven** (Gerenciamento de dependências)
- **JaCoCo** (Cobertura de testes)
- **Lombok** (Redução de boilerplate)
- **Liquibase** (Maior controle sobre organização dos dados) *Pendente

## 🏗️ Arquitetura

O projeto segue uma arquitetura em camadas baseada nos princípios do Domain Driven Design (DDD):

A principio, a divisão de responsabilidades fica:
- Controllers: Formatar a saida para DTO's para que a lógica de domínio não fique exposta
- Services: Garantir o fluxo correto de operações ou reversão das transações em caso de falhas
- Model: Representar bem as entidades e garantir a aplicação das regras de negócio
- Infra: Configurações e manipulação de dados em nivel mais baixo
```
src/
├── main/
│   ├── java/com/gustavo/mobiauto_backend/
│   │   ├── controller/          # Controladores REST
│   │   ├── service/             # Lógica de negócio
│   │   ├── model/               # Entidades de domínio
│   │   ├── infra/               # Infraestrutura (repositórios, exceções)
│   │   └── config/              # Configurações
│   └── resources/
│       └── application.properties
├── test/                        # Testes unitários
└── target/                      # Artefatos de build
```

## 🔧 Funcionalidades

### 👥 Gerenciamento de Usuários
- ✅ Cadastro de usuários (clientes)
- ✅ Autenticação JWT
- ✅ Atualização de dados
- ✅ Ativação/Desativação de contas
- ⚠️ Pendente: Sistema de perfis com roles para usuários

### 🏪 Gerenciamento de Lojas
- ✅ Cadastro de lojas
- ✅ Validação de CNPJ
- ✅ Listagem de lojas ativas
- ✅ Atualização de dados
- ✅ Ativação/Desativação
- ⚠️ Pendente: Sistema de restrição de endpoint para colaboradores


### 🚗 Gerenciamento de Veículos
- ✅ Cadastro de veículos (tipo, modelo, ano, cor)
- ✅ Atualização de especificações
- ✅ Validação de dados

### 💼 Gerenciamento de Ofertas
- ✅ Criação de ofertas (loja + cliente + veículo)
- ✅ Atualização de ofertas
- ✅ Controle de status das ofertas
- ✅ Ativação/Desativação
- ✅ Proteção de integridade referencial
- ⚠️ Pendente: Possibilidade de repasse de oferta entre funcionários
- ⚠️ Pendente: Atribuição automática de ofertas para funcionários de Lojas

## 📡 Endpoints da API

### Autenticação
```
POST /auth/login              # Login do usuário
```

### Usuários
```
POST   /users                 # Cadastrar usuário
GET    /users/{id}            # Buscar usuário por ID
PATCH  /users/{id}            # Atualizar usuário
DELETE /users/{id}            # Desativar usuário
PATCH  /users/{id}/reactivate # Reativar usuário
```

### Lojas
```
POST   /stores                # Cadastrar loja
GET    /stores                # Listar lojas ativas
GET    /stores/{id}           # Buscar loja por ID
PATCH  /stores/{id}           # Atualizar loja
DELETE /stores/{id}           # Desativar loja
PATCH  /stores/{id}/reactivate # Reativar loja
```

### Ofertas
```
POST   /stores/{storeId}/users/{userId}/offers  # Criar oferta
PATCH  /offers/{id}                             # Atualizar oferta
DELETE /offers/{id}                             # Desativar oferta
PATCH  /offers/{id}/reactivate                  # Reativar oferta
```

## 🔐 Segurança

- **Autenticação JWT**: Tokens seguros para autenticação de usuários (Pendente sistema de roles)
- **Validação de dados**: Validação robusta nas camadas, regras de negócio aplicadas nos Modelos
- **Criptografia de senhas**: Senhas hasheadas com Spring Security
**Transactions**: Controle transacional para operações críticas, garantindo consistência dos dados e evitando problemas com paralelismo de operações

## 📊 Qualidade do Código

### Cobertura de Testes
- **Cobertura mínima**: 80% de linhas, 70% de branches
- **Testes unitários**: Cobertura completa dos serviços e regras de negócio (aplicadas no model)
- **Testes de integração**: * Pendente

### Exceções Customizadas
- `EntityNotFoundException`: Entidade não encontrada
- `EntityInUseException`: Entidade em uso (não pode ser removida)
- `DuplicateException`: Duplicação de dados únicos
- `AlreadyActiveException`: Tentativa de ativar entidade já ativa
- `DeactivatedException`: Operação em entidade desativada

## 🛠️ Configuração e Instalação

### Pré-requisitos
- **Java 17+**
- **Maven 3.6+**: ou uso do wrapper anexado com o projeto `./mvnw`
- **Docker & Docker Compose** (ou PostgreSQL)
- **PostgreSQL** (ou usar o Docker)
- - Opcional uso de container com pgadmin4 ou outra solução para debugar os dados diretamente no banco.

### Variáveis de Ambiente
Crie um arquivo `.env` na raiz do projeto:

```env
# Database
DB_USERNAME=postgres
DB_PASSWORD=sua_senha_postgres

# JWT
JWT_SECRET=sua_chave_secreta_jwt_muito_segura_aqui
```

### Instalação

1. **Clone o repositório**
```bash
git clone <url-do-repositorio>
cd mobiauto-backend
```

2. **Configure as variáveis de ambiente**
```bash
cp .env.example .env
# Edite o arquivo .env com suas configurações
```

3. **Inicie o banco de dados com Docker**
```bash
docker compose up -d
```

4. **Execute a aplicação**
```bash
./mvnw spring-boot:run
```

Ou usando Maven:
```bash
mvn spring-boot:run
```

## 🧪 Executando Testes

### Todos os testes
```bash
./mvnw test
```

### Relatório de cobertura
```bash
./mvnw jacoco:report
```
O relatório será gerado em `target/site/jacoco/index.html`

### Verificação de cobertura
```bash
./mvnw jacoco:check
```

## 🐳 Docker

### Subir apenas o banco de dados
```bash
docker compose up postgres -d
```

### Parar os serviços
```bash
docker compose down
```

### Logs
```bash
docker compose logs -f postgres
```

## 🔄 Exemplo de Uso da API

**📋 Para exemplos detalhados com bodies JSON de todas as requisições, veja: [EXAMPLES.md](./EXAMPLES.md)**

## ⏳ O que ficou faltando?

Com o prazo dado para a implementação, alguns recursos desejados para a aplicação não tiveran seu desenvolvimento concluído. Vou deixar um espaço aqui para dizer como tinha pensado em implementar esses recursos.

### Sistema de Perfis

Inicialmente, havia planejado que cada usuário tivesse uma conta única `User`, e essa conta tivese uma lista de `Profiles` que seriam ligados diretamente às Stores. A ideia seria de que cada profile tivesse associação à uma `Store` e uma role associada à essa loja. Isso possibilitaria que funcionários (ou vendedores) não precisassem de múltiplas contas para múltiplas funções (anunciar uma oferta, gerenciar ofertas ou gerenciar funcionários). Isso habilitaria um caso específico unde um `User` pudesse utilizar o mesmo login para atuar como anunciador (usuário básico) ou usuário vendedor ou administrador de uma loja.

### Atribuição automática de ofertas para funcionários

Sem o sistema de Perfis, também não foi possível habilitar o repasse automático de ofertas, mas a ideia era de que funcionasse de seguinte modo:
- Quando uma nova nova oferta fosse criada para uma loja, seria consultado todos os perfis ativos para esta loja, o perfil com a menor quantidade de ofertas com estado "em_andamento" receberia a oferta. Em casos de empate o Perfil com ultima atribuição mais antiga receberia a oferta.
- Quando um perfil funcionário fosse desabilitado, todas as suas ofertas passariam novamente pela função de atribuição.
- Ao reativar uma oferta que está com um funcionário inativo, ela também passaria novamente pela função de atribuição.

### Possivel utilização de uma sistema de mensageria para retentativas ou um projeto de escala maior

Em um sistema um pouco mais maduro e para garantir maior escalabildiade, eu cogitaria o uso de emissão de evento com o RabbitMQ para filas de retentativas ou para garantir o processamento de eventos que propagassem no sistema em uma arquitetura mais descentralizada com microsserviços. Isso possibilitaria tambem o processamento asíncrono dessas operações principais do sistema.



Para dúvidas ou suporte:
- **Email**: gustavo.eugenio@hotmail.com
