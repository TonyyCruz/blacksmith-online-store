# 🛒 Blacksmith Online Store API

## 📖 Sobre o Projeto

A **Blacksmith's Online Store API** é uma aplicação desenvolvida em **Java com Spring Boot** cujo objetivo é gerenciar o fluxo de pedidos, produtos e usuarios em um sistema de e-commerce com tema medieval.

Esta aplicação, foi idealizada como um **projeto pessoal**, focando em **boas práticas de arquitetura**, **segurança com JWT**, e **organização de código**.

---

## 🧩 Funcionalidades Principais

### 👤 Autenticação e Autorização
- Implementação de **Spring Security** com **JWT (Auth0 Java JWT)**.
- Controle de acesso baseado em **roles** (`ADMIN` e `CUSTOMER`).
- Apenas `ADMIN` pode gerenciar produtos, ferreiros e visualizar todos os pedidos.
- Usuários `CUSTOMER` podem criar e visualizar apenas os seus próprios pedidos.
- Criptografia de senhas com **BCryptPasswordEncoder**.

### 🧍 Usuários (`User`)
- Cadastro e autenticação de usuários.
- Validação de idade mínima (18 anos).
- Senha deve possuir os caracteres obrigatórios.
- Para atualizar a senha deve enviar a senha antiga para validação.

### 📦 Armas (`Weapon`)
- Cadastro, atualização e exclusão de produtos (somente `ADMIN`).
- Regras de negócio simples de controle de estoque.
- Associação da arma ao ferreiro que o forjou.
- Clientes podem avaliar os produtos após a compra com notas de 1 a 5.
- Usa o padrão de projeto **Builder** para otimizar a construção da entidade.

### 🧾 Pedidos (`Order`)
- Cálculo automático do valor total do pedido.
- Apenas o cliente pode acessar os seus próprios pedidos.
- Admins têm acesso global para fins de auditoria.

### ⚔️ Ferreiro (`Blacksmith`)
- Não interagem diretamente com a aplicação.
- Pode ser adicionado e editado apenas por admins.
- Tem uma nota avaliativa de 1 a 5 baseada na média das notas das armas forjadas por ele.

---

## ⚙️ Tecnologias Utilizadas

| Categoria | Tecnologias |
|------------|--------------|
| **Linguagem** | Java 17 |
| **Framework principal** | Spring Boot 3 |
| **Segurança** | Spring Security + JWT (Auth0) |
| **Persistência** | Spring Data JPA + Hibernate |
| **Banco de Dados** | PostgreSQL |
| **Build & Dependências** | Maven |
| **Validações** | Jakarta Bean Validation (javax/jakarta.validation) |
| **Documentação** | Swagger / Springdoc OpenAPI |
| **Utilitários** | Lombok, MapStruct |

---

## 🧱 Arquitetura do Projeto

A arquitetura segue o modelo de **camadas** (layered architecture), com separação clara de responsabilidades:
com.anthony.blacksmithOnlineStore <br>
│ <br>
├┬─ controller → Camada de entrada da aplicação (endpoints REST) <br>
│└─ dto → Objetos de transferência de dados (entrada e saída)<br>
├── service → Contém a lógica de negócio <br>
├── repository → Interface com o banco de dados (Spring Data JPA)<br>
├── security → Configuração de segurança e JWT<br>
├── entity → Mapeamento JPA das entidades<br>
├── enums → Enumerações (ex: Role)<br>
├── exception → Exceções personalizadas e handlers globais<br>
└── mapstruct → Para atualização parcial de entidades


Essa estrutura garante:
- Coesão interna em cada camada
- Baixo acoplamento entre componentes
- Facilidade para testes e manutenção

---

## 🔐 Segurança

A autenticação é baseada em **JWT (JSON Web Token)**.  
Após o login bem-sucedido, o usuário recebe um token que deve ser enviado no cabeçalho `Authorization` de cada requisição:
`Authorization: Bearer <seu_token_aqui>`


A autorização é controlada por anotações como:

```java
@PreAuthorize("hasRole('ADMIN')")
@PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
```
---

## 🚀 Como Executar o Projeto
Pré-requisitos

- Java 17+
- Maven 3.8+
- PostgreSQL em execução

### 1️⃣ Clone o repositório
git clone git@github.com:TonyyCruz/blacksmith-online-store.git
cd blacksmith-online-store

### 2️⃣ Configure o banco de dados (Opcional)
#### Edite o arquivo src/main/resources/application.properties:
spring.datasource.url=jdbc:postgresql://localhost:5432/order_management
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
spring.jpa.hibernate.ddl-auto=update

### 3️⃣ Compile e execute
```mvn spring-boot:run``` <br>
Ou diretamente na sua IDE favorita.

---

## 🧠 Decisões Técnicas

- Utilização de DTOs para isolamento entre a API e a camada de persistência.

- Métodos fromEntity() e toEntity() para conversões claras e centralizadas.

- Enum Role implementando GrantedAuthority, garantindo integração limpa com o Spring Security.

- Tratamento de exceções personalizado, retornando respostas claras e padronizadas para o cliente.

- Validações com Bean Validation (ex: idade mínima para cadastro).

- Specifications para filtros dinâmicos em consultas (ex: busca de armas).

- Adicionei nome e ‘id’ do ferreiro em memória na entidade ‘item’ e mudei o fetch para lazy, deixando a consulta de itens mais performática.

- Adicionei métodos de validação de status no OrderStatus para garantir transições de status válidas e centralizar as validações.

- Pelo fato de trabalhar com itens únicos e de pouco estoque, resolvi fazer a dedução do estoque apenas no momento do pagamento, evitando o bloqueio temporário dos itens que ocorreria em caso de dedução imediata do mesmo.

---

## 📘 Exemplos de Endpoints
### Autenticação
`POST /auth/register`

`POST /auth/login`

### Armas
`GET /weapons`

`GET /weapons/id`

`GET /weapons?`

`POST /weapons`        # ADMIN

`PUT /weapons/{id}`    # ADMIN

`PATCH /weapons/{id}`  # ADMIN

`DELETE /weapons/{id}` # ADMIN

### Ferreiros
`GET /ferreiros`

`GET /ferreiros/{id}`

`POST /ferreiros`      #ADMIN

`PUT /ferreiros`       #ADMIN

### Pedidos
`POST /orders`          # CUSTOMER

`GET /orders`           # CUSTOMER (somente os seus pedidos)

`GET /orders/{ID}`      # ADMIN / CUSTOMER (somente os seus pedidos)

### Avaliação
- `POST /api/avaliacoes` → Avaliar arma (apenas os compradores)

- `GET /api/armas/{id}/avaliacoes` → Listar avaliações de uma arma
