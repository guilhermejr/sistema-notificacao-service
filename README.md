# notificacao-service

Microsserviço de **envio de notificações** — e-mail e push — consumido pelos demais serviços do sistema.

## Stack

| Item | Versão |
|---|---|
| Java | 21 |
| Spring Boot | 4.1.1 |
| Spring Cloud | 2025.1.3 |

| Porta | Context path |
|---|---|
| 9007 | `/notificacao-service/` |

## Canais

| Canal | Como |
|---|---|
| **E-mail** | SMTP, via `spring-boot-starter-mail` |
| **Push** | Pushover (`pushover-client`) |
| **Fila** | Amazon SQS, via Spring Cloud AWS |

## Endpoints

| Método | Rota | Descrição |
|---|---|---|
| `POST` | `/autenticacao/enviar-link` | envia o link de recuperação de senha |
| `POST` | `/supermercado/enviar-notificacao-nfe` | avisa que uma NFE foi processada |
| `POST` | `/supermercado/enviar-notificacao-generica` | envia uma notificação avulsa |

## Fila

O `RecuperarSenhaConsumer` escuta a fila SQS de recuperação de senha; o `RecuperarSenhaProducer` publica nela. A URL da fila e o atraso vêm de `aws-queue-esqueci-minha-senha` e `aws-delay-esqueci-minha-senha`, no Vault.

## Configuração

A aplicação não guarda configuração própria: ela busca tudo no arranque, via `spring.config.import`.

| Origem | O que vem de lá |
|---|---|
| **Vault** (`secret/application`) | segredos compartilhados: `JWTSecret`, credenciais de e-mail, AWS, Eureka |
| **Vault** (`secret/<nome-do-serviço>`) | segredos próprios, como as credenciais do banco |
| **Config Server** | `server.port`, `context-path`, datasource e demais propriedades |

Chaves usadas deste serviço, em `secret/application`: `emailHost`, `emailUser`, `emailPass`, `pushoverToken`, `pushoverUser`, `aws-access-key`, `aws-secret-key`, `aws-queue-esqueci-minha-senha`, `aws-delay-esqueci-minha-senha`, `url`.

### Variável de ambiente obrigatória

| Variável | Para que serve |
|---|---|
| `VAULT_TOKEN` | token de acesso ao Vault |

`VAULT_TOKEN` **não tem valor padrão**. Sem ela, o Spring envia a string literal `${VAULT_TOKEN}` ao Vault, recebe `403` e — como `spring.cloud.vault.fail-fast` vem desligado — o erro só aparece bem depois, disfarçado de placeholder não resolvido (`${...} is malformed`). Se quiser que a falha apareça na hora, ligue `spring.cloud.vault.fail-fast: true`.

Também são necessários `VAULT_HOST`, `VAULT_PORT` e `VAULT_SCHEME` quando o Vault não está em `localhost:8200` via `http`, e `CONFIG_SERVER_USER` / `CONFIG_SERVER_PASS` nos serviços que leem do Config Server.

## Como executar

```bash
# build
./mvnw clean package

# execução
VAULT_TOKEN=<seu-token> java -jar target/notificacao-service-*.jar --spring.profiles.active=dev
```

> **Dependências no ar:** este serviço só sobe com o **Vault**, o **Config Server** e o **Eureka** disponíveis, além do seu banco PostgreSQL.

A aplicação sobe em `http://localhost:9007/notificacao-service/`.

### Docker

O `Dockerfile` espera o jar já na raiz do projeto, com o nome `sistema-notificacao-service.jar`:

```bash
./mvnw clean package
cp target/notificacao-service-*.jar sistema-notificacao-service.jar

docker build \
  --build-arg VAULT_HOST=<host> \
  --build-arg VAULT_TOKEN=<token> \
  --build-arg CONFIG_SERVER_USER=<usuario> \
  --build-arg CONFIG_SERVER_PASS=<senha> \
  -t notificacao-service .
```
