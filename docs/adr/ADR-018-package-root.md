# ADR-018: Package root `com.kharcha`

- Status: Accepted
- Date: 2026-09-30

## Context
The spec named the package root `in.kharcha.<module>`. `in` is a reserved keyword in both Java
and Kotlin, so `package in.kharcha.common;` does not compile, and an Android `namespace` of
`in.kharcha.app` would generate an uncompilable `R` class.

## Decision
Use `com.kharcha.<module>` everywhere (backend and Android). Multi-word modules use either a
sub-package or a joined name: `com.kharcha.ingest`, `com.kharcha.mcp.finance`,
`com.kharcha.agentruntime`, `com.kharcha.refundworker`.

## Alternatives considered
- **`in_.kharcha`**: the JLS convention for keyword domains; valid but awkward to read and type.
- **`app.kharcha`**: valid, but less conventional.

## Consequences
CLAUDE.md and spec §5 updated. The Android `applicationId` may still be chosen independently.
