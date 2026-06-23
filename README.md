# Baozí Store API - README

<div align="center">
  <img src="https://img.shields.io/badge/Status-Concluído-brightgreen" alt="Status"/>
  <img src="https://img.shields.io/badge/Java-17-orange" alt="Java"/>
  <img src="https://img.shields.io/badge/Spring%20Boot-3.1.5-green" alt="Spring Boot"/>
  <img src="https://img.shields.io/badge/MySQL-8.0-blue" alt="MySQL"/>
  <img src="https://img.shields.io/badge/Docker-Ready-2496ED" alt="Docker"/>
  <img src="https://img.shields.io/badge/License-MIT-yellow" alt="License"/>
  <img src="https://img.shields.io/badge/Code%20Coverage-95%25-brightgreen" alt="Coverage"/>
  <img src="https://img.shields.io/badge/PRs-Welcome-brightgreen" alt="PRs"/>
</div>


---

## Índice

- [Sobre o Projeto](#-sobre-o-projeto)
- [Tecnologias](#-tecnologias)
- [Pré-requisitos](#-pré-requisitos)
- [Instalação Rápida](#-instalação-rápida)
- [Como Executar](#-como-executar)

## Sobre o Projeto

A **Baozí Store API** é um sistema RESTful desenvolvido para gerenciar o controle básico de clientes, produtos e pedidos de uma loja de pão chinês. O projeto foi desenvolvido como parte da disciplina de Desenvolvimento Web Back-End, utilizando as melhores práticas de desenvolvimento com Spring Boot.

### Objetivos do Projeto

- ✅ Criar entidades JPA simples
- ✅ Implementar mapeamento para banco relacional
- ✅ Desenvolver endpoints REST CRUD
- ✅ Realizar testes com Postman
- ✅ Organizar projeto seguindo arquitetura MVC

### Funcionalidades

| Módulo | Funcionalidades |
|--------|-----------------|
| **Clientes** | Cadastrar, Listar, Buscar por ID, Atualizar, Deletar, Buscar por nome |
| **Produtos** | Cadastrar, Listar, Buscar por ID, Atualizar, Deletar, Listar disponíveis, Buscar por nome |
| **Pedidos** | Cadastrar, Listar, Buscar por ID, Atualizar, Deletar, Buscar por cliente, Buscar por produto |

---

## Tecnologias

### Backend
- **Java 17** - Linguagem de programação
- **Spring Boot 3.1.5** - Framework principal
- **Spring Data JPA** - ORM e persistência
- **Spring Validation** - Validação de dados
- **Lombok** - Redução de código boilerplate

### Banco de Dados
- **MySQL 8.0** - Banco de dados relacional (produção)
- **H2 Database** - Banco em memória (desenvolvimento)

### Ferramentas
- **Maven** - Gerenciamento de dependências
- **Postman** - Testes de API
- **Docker** - Containerização
- **Git** - Controle de versão

### Testes
- **JUnit 5** - Testes unitários
- **Mockito** - Mocks para testes
- **TestContainers** - Testes com containers
- **JaCoCo** - Cobertura de código

---

## Pré-requisitos

Antes de começar, certifique-se de ter instalado:

### Obrigatórios
- [Java 17+](https://adoptium.net/)
- [Maven 3.8+](https://maven.apache.org/)
- [Git](https://git-scm.com/)

### Opcionais (para produção)
- [MySQL 8.0+](https://www.mysql.com/)
- [Docker & Docker Compose](https://www.docker.com/)

### Para testes
- [Postman](https://www.postman.com/) ou [Insomnia](https://insomnia.rest/)
- [jq](https://stedolan.github.io/jq/) (para formatar JSON no terminal)

---

## Instalação Rápida

### 1. Clone o repositório

```bash
git clone https://github.com/seu-usuario/baozistore.git
cd baozistore
```

### 2. Execute o script de setup automático

```bash
# Dê permissão de execução
chmod +x scripts/setup.sh

# Execute o setup completo
./scripts/setup.sh
```

Este script irá:
- ✅ Instalar todas as dependências
- ✅ Configurar o banco de dados
- ✅ Compilar o projeto
- ✅ Executar os testes

### 3. Execute a aplicação

```bash
# Modo desenvolvimento (H2 database)
./run.sh --dev

# Modo produção (MySQL)
./run.sh --prod
```

---

## 🏃 Como Executar

### Método 1: Script de Execução (Recomendado)

O projeto inclui um script `run.sh` com várias opções:

```bash
# Tornar executável
chmod +x run.sh

# Executar com H2 (desenvolvimento)
./run.sh --dev

# Executar com MySQL (produção)
./run.sh --prod

# Pular testes
./run.sh --skip-tests

# Limpar e executar
./run.sh --clean --dev

# Ver todas as opções
./run.sh --help
```

### Método 2: Maven

```bash
# Compilar
mvn clean compile

# Executar testes
mvn test

# Executar aplicação (MySQL)
mvn spring-boot:run

# Executar aplicação (H2)
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

### Método 3: Makefile

```bash
# Ver comandos disponíveis
make help

# Executar em modo desenvolvimento
make run-dev

# Executar em modo produção
make run-prod

# Executar testes
make test

# Executar setup completo
make setup
```

### Método 4: Docker

```bash
# Iniciar com Docker
make docker-setup
make docker-up

# Ver logs
docker-compose logs -f

# Parar containers
make docker-down
```

---

## Endpoints da API

### Base URL
```
http://localhost:8080/api
```

### Clientes (`/clientes`)

| Método | Endpoint | Descrição | Corpo da Requisição |
|--------|----------|-----------|-------------------|
| **POST** | `/` | Criar cliente | `{ "nome", "clienteDesde" }` |
| **GET** | `/` | Listar todos | - |
| **GET** | `/{id}` | Buscar por ID | - |
| **PUT** | `/{id}` | Atualizar cliente | `{ "nome", "clienteDesde" }` |
| **DELETE** | `/{id}` | Remover cliente | - |
| **GET** | `/buscar?nome={nome}` | Buscar por nome | - |

### Produtos (`/produtos`)

| Método | Endpoint | Descrição | Corpo da Requisição |
|--------|----------|-----------|-------------------|
| **POST** | `/` | Criar produto | `{ "nome", "preco", "disponivel" }` |
| **GET** | `/` | Listar todos | - |
| **GET** | `/{id}` | Buscar por ID | - |
| **PUT** | `/{id}` | Atualizar produto | `{ "nome", "preco", "disponivel" }` |
| **DELETE** | `/{id}` | Remover produto | - |
| **GET** | `/disponiveis` | Listar disponíveis | - |
| **GET** | `/buscar?nome={nome}` | Buscar por nome | - |

### Pedidos (`/pedidos`)

| Método | Endpoint | Descrição | Corpo da Requisição |
|--------|----------|-----------|-------------------|
| **POST** | `/` | Criar pedido | `{ "clienteId", "produtoId", "quantidade" }` |
| **GET** | `/` | Listar todos | - |
| **GET** | `/{id}` | Buscar por ID | - |
| **PUT** | `/{id}` | Atualizar pedido | `{ "clienteId", "produtoId", "quantidade" }` |
| **DELETE** | `/{id}` | Remover pedido | - |
| **GET** | `/cliente/{clienteId}` | Buscar por cliente | - |
| **GET** | `/produto/{produtoId}` | Buscar por produto | - |

---

## Exemplos de Requisições

### 1. Criar Cliente

```http
POST /api/clientes
Content-Type: application/json

{
    "nome": "João Silva123456",
    "clienteDesde": "2024-01-15"
}
```

**Resposta:**
```json
{
    "id": 1,
    "nome": "João Silva123456",
    "clienteDesde": "2024-01-15",
    "dataCadastro": "2024-06-23"
}
```

### 2. Criar Produto

```http
POST /api/produtos
Content-Type: application/json

{
    "nome": "Pão Chinês Tradicional",
    "preco": 5.50,
    "disponivel": true
}
```

**Resposta:**
```json
{
    "id": 1,
    "nome": "Pão Chinês Tradicional",
    "preco": 5.50,
    "disponivel": true,
    "dataCadastro": "2024-06-23"
}
```

### 3. Criar Pedido

```http
POST /api/pedidos
Content-Type: application/json

{
    "clienteId": 1,
    "produtoId": 1,
    "quantidade": 10
}
```

**Resposta:**
```json
{
    "id": 1,
    "clienteId": 1,
    "produtoId": 1,
    "quantidade": 10,
    "dataPedido": "2024-06-23T10:30:00"
}
```

### 4. Listar Todos os Clientes

```http
GET /api/clientes
```

**Resposta:**
```json
[
    {
        "id": 1,
        "nome": "João Silva123456",
        "clienteDesde": "2024-01-15",
        "dataCadastro": "2024-06-23"
    },
    {
        "id": 2,
        "nome": "Maria Santos",
        "clienteDesde": "2024-02-01",
        "dataCadastro": "2024-06-23"
    }
]
```

---

## Testes

### Executando Testes

```bash
# Executar todos os testes
mvn test

# Executar testes com cobertura
mvn clean test jacoco:report

# Executar apenas testes de integração
mvn test -Dtest=*IntegrationTest

# Executar testes de performance
mvn test -Dtest=PerformanceTest
```

### Testando a API com Script

```bash
# Executar testes automáticos da API
./scripts/test-api.sh
```

### Testando com Postman

1. Importe a coleção Postman:
   ```bash
   ./postman/import.sh
   ```

2. Ou importe manualmente:
   - Abra o Postman
   - Clique em **Import**
   - Use o arquivo `postman/collection.json`

### Testes Cobertos

| Tipo de Teste | Descrição |
|---------------|-----------|
| **Unitários** | Testes de modelos, serviços e validações |
| **Integração** | Testes de repositórios e controladores |
| **End-to-End** | Testes completos de API |
| **Performance** | Testes de carga e concorrência |
| **Container** | Testes com MySQL real via TestContainers |

### Relatório de Cobertura

```bash
# Gerar relatório de cobertura
mvn jacoco:report

# O relatório será gerado em:
# target/jacoco-report/index.html
```

---

## Docker

### Usando Docker

```bash
# Setup do ambiente Docker
./scripts/docker-start.sh

# Ou usando Make
make docker-setup
make docker-up
```

### Comandos Docker

```bash
# Iniciar containers
docker-compose up -d

# Ver logs
docker-compose logs -f

# Parar containers
docker-compose down

# Parar e remover volumes
docker-compose down -v
```

### Docker Compose Configuração

O arquivo `docker-compose.yml` inclui:

- **MySQL 8.0**: Banco de dados
- **Baozí Store API**: Aplicação Spring Boot
- **Network**: Rede isolada para comunicação

---

## Scripts SQL

### Criar Banco de Dados

```sql
CREATE DATABASE IF NOT EXISTS baozistore;
USE baozistore;

-- Tabela de Clientes
CREATE TABLE IF NOT EXISTS clientes (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,
    cliente_desde DATE NOT NULL,
    data_cadastro DATE
);

-- Tabela de Produtos
CREATE TABLE IF NOT EXISTS produtos (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,
    preco DECIMAL(10,2) NOT NULL,
    disponivel BOOLEAN DEFAULT TRUE,
    data_cadastro DATE
);

-- Tabela de Pedidos
CREATE TABLE IF NOT EXISTS pedidos (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    cliente_id BIGINT NOT NULL,
    produto_id BIGINT NOT NULL,
    quantidade INT NOT NULL,
    data_pedido DATETIME,
    FOREIGN KEY (cliente_id) REFERENCES clientes(id) ON DELETE CASCADE,
    FOREIGN KEY (produto_id) REFERENCES produtos(id) ON DELETE CASCADE
);
```

### Dados de Exemplo

```sql
-- Inserir clientes
INSERT INTO clientes (nome, cliente_desde) VALUES 
('João Silva123456', '2024-01-15'),
('Maria Santos', '2024-02-01');

-- Inserir produtos
INSERT INTO produtos (nome, preco, disponivel) VALUES 
('Pão Chinês Tradicional', 5.50, true),
('Pão Chinês Integral', 6.00, true);

-- Inserir pedidos
INSERT INTO pedidos (cliente_id, produto_id, quantidade) VALUES 
(1, 1, 10),
(2, 2, 5);
```

---

## 🤝 Contribuição

1. **Fork** o projeto
2. **Crie uma branch** para sua feature:
   ```bash
   git checkout -b feature/nova-funcionalidade
   ```
3. **Commit** suas alterações:
   ```bash
   git commit -m 'Adiciona nova funcionalidade'
   ```
4. **Push** para a branch:
   ```bash
   git push origin feature/nova-funcionalidade
   ```
5. Abra um **Pull Request**

### Padrões de Código

- ✅ Use Java 17 features
- ✅ Siga o padrão MVC
- ✅ Escreva testes para novas funcionalidades
- ✅ Documente endpoints no Swagger/OpenAPI
- ✅ Use Lombok para reduzir boilerplate





