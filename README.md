# Kharcha

AI-first money companion for Indian users: captures bank/UPI notifications on Android, turns them
into clean transactions with a Kafka + Spring Boot backend, and uses a team of AI agents to roast
unnecessary spending, predict your "broke date", answer questions about your money and chase
failed-payment refunds.

- Spec: [docs/PROJECT_SPEC.md](docs/PROJECT_SPEC.md) · Progress: [docs/PROGRESS.md](docs/PROGRESS.md) · Decisions: [docs/adr/](docs/adr/)

> Metrics table (§32) will go here once there are real numbers.

## Prerequisites
JDK 21, Docker, Python 3.11+ with [uv](https://docs.astral.sh/uv/), [Ollama](https://ollama.com), Git.
Gradle is **not** needed (the wrapper downloads it). If JDK 21 is missing, Gradle downloads one.

## Run locally

```bash
# 1. Infrastructure: Kafka (+ topics), Postgres+pgvector, Redis, Temporal, Prometheus, Grafana
docker compose -f infra/docker-compose.yml up -d
docker compose -f infra/docker-compose.yml ps          # all healthy; kafka-init "exited (0)"
docker compose -f infra/docker-compose.yml logs kafka-init   # lists the 18 topics

# 2. Backend
cd backend
./gradlew build                 # compile + unit tests
./gradlew integrationTest       # Testcontainers (needs Docker)
./gradlew :ingest-api:bootRun   # runs Flyway V1 against the compose Postgres

# 3. Ollama smoke test (Spring AI → local model)
ollama pull qwen2.5:1.5b
./gradlew :processor:ollamaSmoke

# 4. ML package
cd ../ml
uv sync
uv run pytest
uv run ruff check . && uv run ruff format --check .
uv run kharcha-ml --help
```

On Windows use `gradlew.bat` instead of `./gradlew`.

| UI | URL |
|----|-----|
| Kafka UI | http://localhost:8089 |
| Temporal UI | http://localhost:8233 |
| Prometheus | http://localhost:9090 |
| Grafana | http://localhost:3000 (admin / admin) |

Configuration overrides (all optional): `KHARCHA_DB_URL`, `KHARCHA_DB_USER`, `KHARCHA_DB_PASSWORD`,
`KHARCHA_OLLAMA_URL`, `KHARCHA_OLLAMA_MODEL`; compose passwords via `infra/.env` (see `infra/.env.example`).

## Layout
```
android-app/   Kotlin + Compose app (Phase 1)
backend/       Gradle multi-module: common, agent-runtime + 10 Spring Boot apps (ADR-001)
ml/            Python: datasets, fine-tuning, evals, export (offline only, ADR-014)
infra/         docker-compose, topic script, Prometheus, Grafana (k8s/Terraform in Phase 9)
docs/          spec, progress, ADRs
```
