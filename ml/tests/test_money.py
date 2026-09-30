import pytest

from kharcha_ml.money import rupees_to_paise


# Same cases as backend PaiseTest, so Java and Python agree on amounts.
@pytest.mark.parametrize(
    ("text", "expected"),
    [
        ("349", 34900),
        ("349.00", 34900),
        ("349.5", 34950),
        ("0.01", 1),
        (" 10 ", 1000),
        ("1,20,000.00", 12000000),
        ("1,000,000", 100000000),
        ("120000", 12000000),
    ],
)
def test_parses_rupees(text: str, expected: int) -> None:
    assert rupees_to_paise(text) == expected


@pytest.mark.parametrize(
    "text",
    ["", "abc", "-349", "349.123", "1,2", "Rs.349", "349.", ".50", "12345678901234"],
)
def test_rejects_invalid_amounts(text: str) -> None:
    with pytest.raises(ValueError):
        rupees_to_paise(text)
