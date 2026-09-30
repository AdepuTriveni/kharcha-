# kharcha-ml

Offline ML for Kharcha (spec §15, §19). Runtime inference stays in Java and Kotlin (ADR-014).
This package only trains, evaluates and exports models.

```bash
uv sync                      # create .venv with dev tools
uv run kharcha-ml --help     # CLI
uv run pytest                # tests
uv run ruff check . && uv run ruff format --check .
```

| Package | Purpose | Spec |
|---------|---------|------|
| `dataset/` | build, synthesize, dedupe, split by sender template | §15.2–15.3 |
| `distill/` | teacher labelling + human review queue | §15.2 |
| `finetune/` | LoRA configs + trainer | §15.4 |
| `eval/` | parser, risk and offline bandit evals | §15.5, §19.3 |
| `export/` | merge LoRA → GGUF + quantize; LightGBM → ONNX | §15.6 |
| `risk/` | risk features + training | §19.1 |

Rules: money is `int` paise; gold eval sets are never used for training (§15.3); training data
only from users with `ml_consent = true`, redacted. Real data lives in `data/` (gitignored);
only synthetic samples go in `data/samples/`.
