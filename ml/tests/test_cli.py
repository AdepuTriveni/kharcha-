import importlib

import pytest

from kharcha_ml.cli import COMPONENTS, main


def test_help_exits_cleanly(capsys: pytest.CaptureFixture[str]) -> None:
    with pytest.raises(SystemExit) as exit_info:
        main(["--help"])
    assert exit_info.value.code == 0
    assert "kharcha-ml" in capsys.readouterr().out


def test_info_lists_every_component(capsys: pytest.CaptureFixture[str]) -> None:
    assert main(["info"]) == 0
    out = capsys.readouterr().out
    for name in COMPONENTS:
        assert name in out


@pytest.mark.parametrize("name", sorted(COMPONENTS))
def test_component_packages_are_importable(name: str) -> None:
    importlib.import_module(f"kharcha_ml.{name}")
