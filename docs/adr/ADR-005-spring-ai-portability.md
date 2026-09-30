# ADR-005: Spring AI for model portability

- Status: Accepted
- Date: 2026-09-30

## Context
LLM calls run on Ollama locally (our own GGUF parser, embeddings) and on a free API tier when
deployed. Agents need per-agent model routing (§16.6), and MCP client/server support (§17).
The spec asks for the latest Spring Boot that Spring AI supports (§6).

## Decision
Use Spring AI with a pinned, matched pair of versions, checked on Maven Central on 2026-09-30:

| Library | Version | Why |
|---------|---------|-----|
| Spring AI | 2.0.1 | latest GA (2.1.0 is still a milestone) |
| Spring Boot | 4.1.1 | the version Spring AI 2.0.1 is built against |
| Gradle | 9.8.0 | current release; wrapper committed |
| Java | 21 | project standard (toolchain; auto-provisioned via foojay) |

Versions live in `backend/gradle/libs.versions.toml`. Starter for local models:
`spring-ai-starter-model-ollama`; configured via `spring.ai.ollama.base-url` and
`spring.ai.ollama.chat.model` (the older `chat.options.model` is deprecated in 2.0).

## Alternatives considered
- **LangChain4j**: capable, but Spring AI integrates more directly with Boot auto-configuration and has first-party MCP starters.
- **Spring Boot 3.5 + Spring AI 1.1**: older, stable line; rejected because the spec asks for the latest supported pair and 2.0 is GA.
- **Direct HTTP clients per provider**: no portability, more code.

## Consequences
- Boot 4 brings Jackson 3 (`tools.jackson.*`), modular starters (e.g. `spring-boot-starter-flyway`,
  `spring-boot-starter-webmvc`) and Testcontainers 2 (`org.testcontainers.postgresql.*`).
- Upgrade Boot and Spring AI together; re-check compatibility at the start of each phase (§6).
