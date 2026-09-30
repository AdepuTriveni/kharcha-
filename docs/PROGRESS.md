# Kharcha — Progress Tracker (v2)

> Tick a box only when the "Done when" criteria in `PROJECT_SPEC.md` §34 pass.
> Claude Code: update this file after each task; record deferred items at the bottom.

## Prerequisites (§33.1)
- [ ] Laptop tools installed (JDK 21, Android Studio, Docker, Python + uv, Ollama, Terraform, kubectl)
- [ ] Accounts: GitHub, Telegram bot, Firebase, Kaggle/Colab, cloud free tier, free LLM API key, Langfuse
- [ ] Test phone ready (Android 10+)

## Phase 0 — Setup
- [ ] Repo skeleton + Gradle multi-module
- [ ] docker-compose + topic init script
- [ ] Flyway V1
- [ ] CI build (backend, Android, Python)
- [ ] Ollama smoke test via Spring AI
- [ ] `ml/` package skeleton (uv, ruff, pytest)

## Phase 1 — Usable by me
- [ ] W1 Capture + basic redaction + Room outbox + list screen
- [ ] W2 Upload API + raw-events + teacher-LLM parser + validation + IT test
- [ ] W3 Telegram bot, daily summary, first trigger, chat cash entry
- [ ] ✅ Using it daily

## Phase 2 — Correct data
- [ ] W4 Dedup, merchants, categories, labeling CLI, first parsing eval
- [ ] W5 Rules + synthesis + shadow + DLTs + metrics
- [ ] W6 Cash wallet, quick-add, widget, redaction corpus, corrections

## Phase 3 — Forecast, harness, friends
- [ ] W7 Forecast + what-if + backtest
- [ ] W8 Agent runtime (limits, permissions, transcripts, grounding, replay) + Coach v1 + policy gate
- [ ] W9 Auth + ArchUnit + early cloud deploy + friends onboarded with consent

## Phase 4 — Own model (Pillar 1)
- [ ] W10 Synthetic generator, distillation, review queue, template splits, dataset card, leakage test
- [ ] W11 Base-model comparison, LoRA fine-tune, five-way eval report
- [ ] W12 GGUF + quantization, Ollama tier 3, Android llama.cpp + constrained decoding, shadow rollout
- [ ] ✅ Model passes promotion gate and runs on phone

## Phase 5 — Multi-agent + MCP + memory (Pillars 2 & 5)
- [ ] W13 Four MCP servers + scoped tokens + permission tests; Coach on MCP; external MCP tokens
- [ ] W14 agent-tasks/results, orchestrator, Cash Detective, Memory Keeper skeleton
- [ ] W15 pgvector memory read/write, memory UI/commands, commitments, memory eval

## Phase 6 — Ask Kharcha + multimodal (Pillars 3 & 6)
- [ ] W16 Analyst views + RLS, SQL validator, tools, charts, /v1/ask, Telegram /ask, app chat
- [ ] W17 120-question eval + improvement loop; bill photo; Hinglish voice + evals

## Phase 7 — Prediction + bandit (Pillar 4)
- [ ] W18 Risk features, LightGBM, ONNX export, Java scoring, calibration report
- [ ] W19 Thompson sampling + constraints + propensities + rewards + IPS evaluation

## Phase 8 — Refund Watchdog
- [ ] refund-rules.yaml verified against latest RBI circular (date: ____)
- [ ] RefundCaseWorkflow + time-skipping tests
- [ ] Refund Advocate drafts + screens

## Phase 9 — Production cloud and scale
- [ ] W21 Terraform, k3s, Strimzi, Postgres, Redis, Temporal, Ollama, CD, dashboards, CI eval gates
- [ ] W22 Load test report; chaos 0 lost / 0 duplicated

## Phase 10 — Proof and presentation
- [ ] §32 metrics table filled with real numbers
- [ ] Competitor comparison table in README
- [ ] Design doc + ADRs (§35)
- [ ] Demo video
- [ ] (Optional) Blog post + Hugging Face model card

## Notes / deferred items
- 2026-09-30 Phase 0 code written on a machine without JDK/Docker/uv/Ollama, so nothing is
  verified yet. Boxes stay unticked until these pass on a machine with the tools:
  `docker compose -f infra/docker-compose.yml up -d` (all healthy, kafka-init exit 0),
  `cd backend && ./gradlew build integrationTest`, `./gradlew :processor:ollamaSmoke`,
  `cd ml && uv sync && uv run pytest && uv run ruff check .`, and CI green on GitHub.
- Verified locally: topic script (stub Kafka CLI, 9 topics + 9 DLTs), `kharcha_ml` CLI and
  money parser (plain Python, no pytest). Gradle wrapper jar SHA-256 matches gradle.org.
- Package root changed to `com.kharcha` (ADR-018): `in` is a Java/Kotlin keyword.
- Versions: Spring Boot 4.1.1 + Spring AI 2.0.1, Gradle 9.8.0 (ADR-005).
- Commit `ml/uv.lock` after the first `uv sync`.
