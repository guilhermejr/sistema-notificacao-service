# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## What this is

Sends notifications — email and push — on behalf of the other services.

| | |
|---|---|
| Port | `9007` |
| Context path | `/notificacao-service` |

Part of a personal microservices system; sibling repos live at `../sistema-*`. The API gateway fronts it at `https://sistema-backend.guilhermejr.net/notificacao-service`.

## Not exposed at the edge

The gateway has **no route** for this service; requests from outside get a 404. It is called internally by the other services through Eureka.

## Channels

| Channel | How |
|---|---|
| Email | SMTP, `spring-boot-starter-mail` |
| Push | Pushover (`pushover-client`) |
| Queue | Amazon SQS, Spring Cloud AWS **4.x** (the 3.x line does not work on Boot 4) |

`RecuperarSenhaConsumer` listens on the password-recovery SQS queue; `RecuperarSenhaProducer` publishes to it. Queue URL and delay come from `aws-queue-esqueci-minha-senha` and `aws-delay-esqueci-minha-senha` in Vault.

## No security

This service has no `spring-boot-starter-security`. It is reachable only from inside the Docker network.

## Actuator

`/actuator/health` is public and returns the status only (`show-details: never`, set in the shared config repo). Everything else under `/actuator/**` requires HTTP Basic with `ROLE_ACTUATOR`, whose credentials come from Vault.

## Configuration comes from outside

This service stores almost no configuration of its own. `application.yml` only bootstraps `spring.config.import`, which pulls from:

- **Vault** — `secret/application` (shared: `JWTSecret`, actuator credentials, mail, AWS) and `secret/<service-name>` (its own DB credentials)
- **Config Server** — `server.port`, `server.servlet.context-path`, datasource, JPA settings

Both must be reachable or the service will not start.

`VAULT_TOKEN` is required and **has no default**. Without it Spring sends the literal string `${VAULT_TOKEN}` to Vault, gets a 403 that Spring Cloud Vault swallows (`fail-fast` is off), and the startup fails much later with a misleading `${someProperty} is malformed`. If you are chasing a confusing startup error, check `VAULT_TOKEN` first.

## Building and running

Java **21 only**. The Homebrew default JDK on this machine is newer and will break the build:

```bash
export JAVA_HOME=/Users/guilhermejr/Library/Java/JavaVirtualMachines/openjdk-21.0.2/Contents/Home
./mvnw clean package
VAULT_TOKEN=<token> java -jar target/*.jar --spring.profiles.active=dev
```

Do not raise `java.version` past 21 while ModelMapper is a dependency — the ByteBuddy bundled in it cannot generate classes on JDK 24+, and the app dies building its mappers with an `UnsupportedOperationException` that does not name the real cause.

## Deploying

`git push origin main` **is** the deploy. A `post-receive` hook on the VPS checks out, runs `mvn clean package` inside a throwaway `maven:3.9-amazoncorretto-21` container, builds the image and restarts it via docker compose. There is no separate release step.

Because the build happens on the VPS, any dependency from a private repository needs credentials **there**, not locally — the hook mounts `/home/guilhermejr/.m2` and passes `GITHUB_TOKEN`.

The `Dockerfile` only copies a prebuilt jar; it carries a `HEALTHCHECK` that polls `/actuator/health`.
