# ADR-001: Modular monolith with separate deployables

- Status: Accepted
- Date: 2026-09-30

## Context
Kharcha has many backend responsibilities (ingest, parsing, insights, agents, four MCP servers,
notifier, refund workflows). It is built by one person at 15–20 h/week and must run on free
tiers, yet should show realistic service boundaries (Kafka between stages, independent scaling).

## Decision
One Gradle multi-module build (`backend/`, Kotlin DSL, version catalog) containing:
- two libraries: `common` (event envelope, topics, IDs, money/time, Flyway migrations) and
  `agent-runtime` (agent harness);
- ten Spring Boot apps, each its own deployable: `ingest-api`, `processor`, `insights`, `agents`,
  `mcp-finance`, `mcp-memory`, `mcp-notify`, `mcp-refund`, `notifier`, `refund-worker`.

Shared build setup (Java 21 toolchain, Spring Boot + Spring AI BOMs, test tasks) lives in the
root `build.gradle.kts`; each module declares its own plugins and dependencies.
Flyway migrations live in `common` so all services see one schema; only `ingest-api` runs them.

## Alternatives considered
- **True microservices, one repo each**: too much overhead (versioning, CI, shared code) for one developer.
- **Single Spring Boot app**: simplest, but hides the Kafka boundaries and per-stage scaling the project is meant to show.

## Consequences
- One `./gradlew build` compiles and tests everything; refactors across modules are atomic.
- Modules must not reach into each other's internals; ArchUnit rules (W9) enforce boundaries.
- Ten apps means ten ports locally (8080–8085, 8091–8094); the local stack stays light by
  running only the apps being worked on.
