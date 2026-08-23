
![Blacksmith Online Store](https://github.com/TonyyCruz/blacksmith-online-store/assets/banner.png)

# ⚒️ Blacksmith Online Store

> **Uma API REST de e-commerce medieval desenvolvida com Java e Spring Boot.**

O **Blacksmith Online Store** é uma API REST de e-commerce inspirada em um mercado medieval, onde 
ferreiros podem vender os seus itens, enquanto clientes podem realizar pedidos, avaliações e 
acompanhar o processo de entrega.

O projeto foi desenvolvido com foco em **boas práticas de desenvolvimento de APIs REST, segurança, 
persistência de dados, transações, eventos e arquitetura de aplicações Spring**.

---

## 🖼️ Preview

![Blacksmith Online Store](https://github.com/TonyyCruz/blacksmith-online-store/assets/preview.png)

---

## 🚀 Tecnologias

### Backend

* ☕ Java
* 🌱 Spring Boot
* 🔐 Spring Security
* 🎟️ JWT
* 🗄️ Spring Data JPA
* 🐘 PostgreSQL
* 📚 Hibernate
* 📖 SpringDoc OpenAPI / Swagger
* 🧪 JUnit
* 🔄 Spring Events
* ⚡ Spring Async
* 🗺️ MapStruct

### Ferramentas

* Git / GitHub
* Insomnia
* Maven
* Docker

---

### ⚒️ Funcionalidades

<details>
<summary>Detalhes</summary>

<br>

<details>
<summary>🔐 Autenticação e autorização</summary>

* Cadastro de usuários
* Login
* Autenticação utilizando JWT
* Controle de acesso baseado em roles
* Roles `CUSTOMER` e `ADMIN`
* Proteção dos endpoints através do Spring Security

</details>

<details>
<summary>⚒️ Ferreiros</summary>

* Cadastro de ferreiros
* Consulta de ferreiros
* Atualização de dados
* Remoção de ferreiros
* Associação de itens aos seus respectivos ferreiros
* Sistema de avaliação

</details>

<details>
<summary>🗡️ Itens</summary>

* Cadastro de itens
* Consulta de itens
* Atualização de itens
* Remoção de itens
* Controle de estoque
* Preços
* Sistema de avaliações
* Filtros utilizando `Specification`
* Paginação e ordenação

</details>


<details>
<summary>🛒 Pedidos</summary>

O sistema permite que clientes criem pedidos contendo múltiplos itens.

Durante a criação do pedido, os dados importantes do produto são armazenados em um 
**snapshot através da entidade `OrderItem`**.

Isso evita que alterações futuras no produto afetem pedidos já realizados.

Por exemplo:

```text
Item atual
 ├── name: Iron Sword
 ├── price: 150.00
 └── stock: 10

        ↓ compra

OrderItem (snapshot)
 ├── name: Iron Sword
 ├── quantity: 2
 ├── unitPrice: 150.00
 └── totalPrice: 300.00
```

Dessa maneira, mesmo que o preço do produto seja alterado posteriormente, o pedido mantém o valor 
original da compra.

</details>

<details>
<summary>💰 Pagamentos</summary>

O projeto possui um fluxo de pagamento simulado para representar o processamento de uma compra.

Fluxo simplificado:

```text
Cliente
   │
   ▼
Criação do pedido
   │
   ▼
Pagamento ◀―――――――――――――――――――┑
   │                          │
   ▼                          │
Processamento do pagamento    │
   │                          │
   ├── Pagamento aprovado     │
   │       │                  │
   │       ▼                  │
   │   Pedido confirmado      │
   │                          │
   └── Pagamento recusado     │
           ├──――――――――――――――――┛
           ▼
       Pedido cancelado
```

</details>

<details>
<summary>📦 Entregas</summary>

Após a confirmação do pagamento, o sistema utiliza eventos para iniciar o processo de entrega.

Fluxo:

```text
Pagamento aprovado
        │
        ▼
Evento de pedido pago
        │
        ▼
Serviço de entrega
        │
        ▼
Entrega criada
        │
        ▼
Simulação de processamento
        │
        ▼
Entrega finalizada
```

A entrega é simulada utilizando processamento assíncrono e um atraso artificial para representar o 
tempo necessário para o envio.

</details>

<details>
<summary>⭐ Avaliações</summary>

Clientes podem avaliar itens após uma compra.

As avaliações também influenciam a classificação do:

* Item
* Ferreiro

O processamento utiliza eventos, que após capturados, atualizam as médias de avaliação dos itens e 
ferreiros.

</details>
</details>

<br>

---

## 📚 Arquitetura, eventos e segurança

<details>
<summary>Detalhes</summary>

### 🧩 Arquitetura

<details>
<summary>Detalhes</summary>
O projeto segue uma organização baseada nas principais responsabilidades da aplicação:

```text
src/main/java
├── com.anthony.blacksmithOnlineStore
│
├┬─ controller → Camada de entrada da aplicação (endpoints REST) <br>
│├─ dto  → Objetos de transferência de dados (entrada e saída)<br>
│└─ docs → Interfaces para agrupar a documentação dos controllers<br>
├── service → Contém a lógica de negócio <br>
├── repository → Interface com o banco de dados (Spring Data JPA)<br>
├── security → Configuração de segurança e JWT<br>
├── entity → Mapeamento JPA das entidades<br>
├── enums → Enumerações (ex: Role)<br>
├── events → Eventos e seus listeners<br>
├── exception → Exceções personalizadas e handlers globais<br>
└── mapstruct → Para atualização parcial de entidades


```

A aplicação utiliza uma separação entre:

```text
Controller
    │
    ▼
Service
    │
    ▼
Repository
    │
    ▼
Database
```

Os controllers são responsáveis pela camada HTTP, enquanto a regra de negócio permanece nos services.
Essa estrutura garante:
- Coesão interna em cada camada
- Baixo acoplamento entre componentes
- Facilidade para testes e manutenção

</details>

---

### 🔄 Eventos

<details>
<summary>Detalhes</summary>

O projeto utiliza o sistema de eventos do Spring para desacoplar algumas operações.

Exemplo:

```text
RatingService
     │
     │ RatingCreatedEvent
     ▼
Event Listener
     │
     ├── Atualiza avaliação do Item
     │
     └── Atualiza avaliação do Blacksmith
```

Esse modelo permite que a criação da avaliação não precise conhecer diretamente todos os componentes 
responsáveis por atualizar as avaliações.

</details>

---

### 🔒 Segurança

<details>
<summary>Detalhes</summary>

A autenticação da API utiliza **JWT (JSON Web Token)**.

Fluxo:

```text
POST /auth/login
       │
       ▼
Credenciais validadas
       │
       ▼
JWT gerado
       │
       ▼
Cliente envia token
       │
       ▼
Authorization: Bearer <token>
       │
       ▼
JwtFilter
       │
       ▼
Spring Security
       │
       ▼
Endpoint protegido
```

Os endpoints são protegidos de acordo com as permissões do usuário.

</details>

---

### 🔎 Filtros

<details>
<summary>Detalhes</summary>

A consulta de itens utiliza `Specification` do Spring Data JPA para permitir filtros dinâmicos.

Exemplo:

```text
GET /items

?name=sword
&minPrice=100
&maxPrice=500
&page=0
&size=10
&sort=price,asc
```

Isso permite combinar diferentes critérios sem precisar criar um método de repository para cada 
combinação possível.

</details>
</details>

---

## 📒 Documentação da API

<details>
<summary>Detalhes</summary>

A API possui documentação utilizando **OpenAPI / Swagger**.

Após iniciar a aplicação, a documentação pode ser acessada em:

```text
http://localhost:8080/swagger-ui/index.html
```

A documentação contém:

* Endpoints
* Parâmetros
* DTOs
* Respostas HTTP
* Autenticação Bearer Token
* Códigos de erro
* Exemplos de requisições

</details>

---

## 🧪 Testes

<details>
<summary>Detalhes</summary>

O projeto possui testes para validar os principais fluxos da aplicação.

Entre os cenários testados estão:

* Autenticação
* Criação de pedidos
* Processamento de pagamentos
* Avaliações
* Atualização de ratings
* Fluxos envolvendo eventos

Os testes também são utilizados para validar o comportamento da aplicação com transações e 
persistência JPA.

Para executar os testes:

```bash
./mvnw test
```

No Windows:

```bash
mvnw.cmd test
```

</details>

---

## 🗄️ Banco de dados

<details>
<summary>Detalhes</summary>

O projeto utiliza **PostgreSQL** como banco de dados relacional.

O mapeamento das entidades é realizado através do:

* Spring Data JPA
* Hibernate
* JPA

Principais entidades:

```text
User
 │
 ├── Orders
 │
 └── Ratings

Blacksmith
 │
 └── Items

Order
 │
 └── OrderItems

Item
 │
 └── Ratings
```

<details>
<summary>📷 Snapshot de pedidos</summary>

Uma das decisões importantes do projeto é o uso de `OrderItem` como snapshot.

```text
Item
 ├── id
 ├── name
 ├── price
 └── ...

          ↓

OrderItem
 ├── itemId
 ├── name
 ├── unitPrice
 ├── quantity
 └── totalPrice
```

Isso garante a preservação das informações relevantes no momento da compra.

</details>
</details>

---

## 🧠 Decisões Técnicas

<details>
<summary>Detalhes</summary>

- Utilização de DTOs para isolamento entre a API e a camada de persistência.

- Métodos `fromEntity()` e `toEntity()` para conversões claras e centralizadas.

- Enum Role implementando GrantedAuthority, garantindo integração limpa com o Spring Security.

- Tratamento de exceções personalizado, retornando respostas claras e padronizadas para o cliente.

- Validações com Bean Validation (ex: idade mínima para cadastro).

- Specifications para filtros dinâmicos em consultas (ex: busca de armas).

- Adicionei `name` e `id` do ferreiro em memória na entidade `item` e mudei o fetch para lazy, 
deixando a consulta de itens mais performática.

- Adicionei métodos de validação de status no OrderStatus para garantir transições de status válidas 
e centralizar as validações.

- Pelo fato de trabalhar com itens únicos e de pouco estoque, resolvi fazer a dedução do estoque 
apenas no momento do pagamento, evitando o bloqueio temporário dos itens que ocorreria em caso de 
dedução imediata do mesmo.

- Utilizei eventos nas avaliações dos itens. Quando o evento é capturado, ele atribui a nota 
recebida ao item comprado e ao ferreito que o forjou.

- Adicionei um evento no pagamento da compra. O evento é capturado por dois listeners, um que 
atualiza o estoque e o status do pedido, e outro que aciona a simulação de entrega.

</details>

---

## ⚙️ Como executar

### Clone o projeto

```bash
git clone https://github.com/TonyyCruz/blacksmith-online-store.git
```

Entre no diretório(raiz do projeto):

```bash
cd blacksmith-online-store
```

<details>
<summary>🐋 Rodando no Docker</summary>

Na raiz do projeto, execute:

```jsx
  docker compose up -d --build
```

- Esse serviço irá inicializar dois containers chamados `blacksmith_api` e outro chamado 
`blacksmith_api_db`.

- A partir daqui você pode acessar o container via CLI ou abri-lo na sua IDE.

Para acessar via CLI use o comando
```jsx
docker exec -it blacksmith_api bash
```
- Com esse comando, você terá acesso ao terminal interativo do container blacksmith_api criado pelo 
compose, que está rodando em segundo plano.

<details>
<summary>🗑️ Para remover os containers</summary><br />

- Somente containers
```jsx
docker compose down
```

- Containers + imagens
```jsx
docker compose down -v
```

⚠️ `docker compose down -v` remove os volumes, portanto os dados persistidos no banco serão perdidos.

</details>
</details>

<details>
<summary>💻 Rodando Localmente</summary>

### Pré-requisitos
Antes de iniciar a aplicação, certifique-se de possuir:
* Java instalado
* Maven
* PostgreSQL

### Configure o banco

Crie um banco PostgreSQL para a aplicação.

Exemplo:

```sql
CREATE DATABASE blacksmith_online_store;
```

Configure as propriedades de conexão no arquivo de configuração da aplicação:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/blacksmith_online_store
spring.datasource.username=SEU_USUARIO
spring.datasource.password=SUA_SENHA
```

### Execute a aplicação

Linux/macOS:

```bash
./mvnw spring-boot:run
```

Windows:

```bash
mvnw.cmd spring-boot:run
```

A API estará disponível em:

```text
http://localhost:8080
```

</details>

<br>

---

## 📡 Exemplos de requisições

<details>
<summary>Detalhes</summary>

### Login

```http
POST /auth/login
Content-Type: application/json
```

```json
{
  "email": "customer@email.com",
  "password": "Password01#"
}
```

---

### Criar pedido

```http
POST /orders
Authorization: Bearer <token>
Content-Type: application/json
```

```json
{
  "items": [
    {
      "itemId": "item-id",
      "quantity": 2
    }
  ]
}
```

</details>

---

## 🗺️ Roadmap

* [x] Autenticação JWT
* [x] Spring Security
* [x] CRUD de itens
* [x] CRUD de ferreiros
* [x] Sistema de pedidos
* [x] Snapshot de `OrderItem`
* [x] Sistema de avaliações
* [x] Eventos do Spring
* [x] Simulação de pagamento
* [x] Simulação de entrega
* [x] Swagger / OpenAPI
* [x] Paginação
* [x] Filtros dinâmicos
* [x] Docker / Docker Compose
* [ ] Separar pagamento e entrega em microserviços
* [ ] Pipeline CI/CD
* [ ] Melhorias nos testes de integração
* [ ] Sistema de cupons e descontos
* [ ] Integração com gateway de pagamento real

---

## 🎯 Objetivos do projeto

Este projeto foi desenvolvido com o objetivo de demonstrar minhas habilidades no desenvolvimento
backend utilizando Java e Spring Boot.

Entre os principais desafios abordados estão:

- Desenvolvimento de uma API REST completa
- Autenticação e autorização utilizando JWT
- Modelagem de relacionamentos utilizando JPA/Hibernate
- Gerenciamento de transações
- Processamento assíncrono e baseado em eventos
- Controle de concorrência e estoque
- Desenvolvimento de testes automatizados
- Containerização com Docker
- Documentação da API utilizando OpenAPI

---

## 👨‍💻 Autor

**Anthony Cruz**

Desenvolvedor focado em Backend com Java e Spring Boot.

### 🔗 Links

* GitHub: https://github.com/TonyyCruz
* Projeto: https://github.com/TonyyCruz/blacksmith-online-store

---

## 📄 Licença

Este projeto está disponível para fins de estudo e portfólio.
