"""Command-line entry point: ``uv run kharcha-ml --help``."""

from __future__ import annotations

import argparse

from kharcha_ml import __version__

# Subpackage -> what it does (spec section). Keep in sync with README.md.
COMPONENTS: dict[str, str] = {
    "dataset": "build, synthesize, dedupe, split by sender template (spec 15.2-15.3)",
    "distill": "teacher labelling + human review queue (spec 15.2)",
    "finetune": "LoRA training configs + trainer (spec 15.4)",
    "eval": "parser, risk and offline bandit evals (spec 15.5, 19.3)",
    "export": "merge LoRA, GGUF + quantize; LightGBM to ONNX (spec 15.6)",
    "risk": "risk feature extraction + training (spec 19.1)",
}


def build_parser() -> argparse.ArgumentParser:
    parser = argparse.ArgumentParser(
        prog="kharcha-ml",
        description="Offline ML tooling for Kharcha (datasets, fine-tuning, evals, export).",
    )
    parser.add_argument("--version", action="version", version=f"%(prog)s {__version__}")
    commands = parser.add_subparsers(dest="command", metavar="COMMAND")
    commands.add_parser("info", help="list ML components and the spec sections they implement")
    return parser


def main(argv: list[str] | None = None) -> int:
    parser = build_parser()
    args = parser.parse_args(argv)
    if args.command == "info":
        for name, description in COMPONENTS.items():
            print(f"{name:<10} {description}")
        return 0
    parser.print_help()
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
