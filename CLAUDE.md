# CLAUDE.md — Kharcha

> Project memory for Claude Code. Keep this file short. The full specification is
> `docs/PROJECT_SPEC.md` (numbered sections, e.g. §15). Progress: `docs/PROGRESS.md`.

## What this project is
Kharcha is an AI-first money companion for Indian users. An Android app captures bank/UPI
notifications (and SMS in a sideload build) in the background; a Spring Boot + Kafka
backend turns them into clean transactions (including cash); and a team of AI agents
roasts unnecessary spending in Hinglish, predicts the user's "broke date", nudges before
risky moments, answers questions about the user's money, remembers commitments, and
chases failed-payment refunds under RBI rules. Parsing uses **our own fine-tuned small
model**, which can run on-device.

## Repo map
```
android-app/     Kotlin + Compose: capture, redaction, on-device model, cash entry, widget
backend/
  common/        Event envelope, topics, DTOs, money/time utils
  ingest-api/    REST API (phone uploads, Ask, bill photos, settings)
  processor/     Parsing (rules → own model → teacher LLM), dedup, merchants, cash
  insights/      Forecast, risk scoring (ONNX), bandit, triggers
  agent-runtime/ Shared agent harness: loop, budgets, tool permissions, transcripts, replay
  agents/        Orchestrator + Coach, Cash Detective, Refund Advocate, Analyst, Memory Keeper
  mcp-finance/   MCP server: read-only finance tools (internal agents + external clients)
  mcp-memory/    MCP server: memory search/write
  mcp-notify/    MCP server: propose messages (never sends directly)
  mcp-refund/    MCP server: refund cases + drafts
  notifier/      Policy gate, Telegram, FCM, feedback capture
  refund-worker/ Temporal workflows
ml/              Python: datasets, distillation, fine-tuning, evals, GGUF/ONNX export
infra/           docker-compose, k8s, Terraform
tools/           Labeling CLI, load tests, chaos scripts
docs/            PROJECT_SPEC.md, PROGRESS.md, adr/
```

## Commands
```bash
docker compose -f infra/docker-compose.yml up -d        # Kafka, Postgres(+pgvector), Redis, Temporal, Prometheus, Grafana
cd backend && ./gradlew build                           # compile + unit tests
cd backend && ./gradlew integrationTest                 # Testcontainers
cd backend && ./gradlew :processor:ollamaSmoke          # Spring AI -> local Ollama (needs `ollama pull qwen2.5:1.5b`)
cd backend && ./gradlew :processor:evalParsing          # parsing eval report
cd backend && ./gradlew :agents:agentRegression         # replay-based agent regression
cd backend && ./gradlew :agents:evalAsk                 # Ask Kharcha eval set
cd android-app && ./gradlew assembleSideloadDebug
cd ml && uv sync && uv run python -m kharcha_ml.eval_parser --model <path>
```

## Non-negotiable rules
1. **Money is `long` paise** in Java/Kotlin and `int` paise in Python. Never floating point.
2. **Time: store UTC (`Instant`), display IST (`Asia/Kolkata`).**
3. **Every Kafka consumer is idempotent** (`processed_events`). Kafka key = `userId`.
4. **Never log raw message text or full account numbers.** Log `eventId`.
5. **AI never moves money, never submits anything to banks/regulators, never sends a
   message directly.** Agents only *propose*; the notifier's policy gate decides.
6. **AI never calculates money.** Numbers in AI-generated text must come from tool results;
   the grounding check (§27.4) rejects any number not present in tool outputs.
7. **Every data access is scoped by `userId`** from the auth token — HTTP, SQL (RLS), MCP.
8. **Gold evaluation sets are never used for training.** Split by sender template (§15.3).
9. **Training data only from users who consented** (`users.ml_consent = true`), redacted.
10. Schema changes via Flyway; event changes bump `schemaVersion` and update §7.
11. No secrets in git.

## Conventions
- Java 21, Gradle Kotlin DSL, package root `com.kharcha.<module>` (ADR-018; `in` is a Java keyword); records for DTOs;
  constructor injection; profiles `local`, `test`, `prod`; integration tests `*IT.java`.
- Kotlin app: MVVM, Hilt, Room, WorkManager, Coroutines/Flow.
- Python (`ml/`): 3.11+, `uv`, `ruff`, `pytest`, type hints; scripts are CLIs with `--help`.
- Prompts live in versioned files under `src/main/resources/prompts/<agent>/vN.md`.
- Significant decisions → ADR in `docs/adr/`.

## How to work
- Read the matching §section before implementing. Follow `docs/PROGRESS.md` in order.
- Show a short plan before writing code for any task larger than one file.
- Keep `main` green; small commits (`feat(agents): ...`).
- If the spec is ambiguous or seems wrong, stop and ask.
