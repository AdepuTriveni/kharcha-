"""Money is ``int`` paise (CLAUDE.md rule 1). Mirrors ``com.kharcha.common.money.Paise``."""

from __future__ import annotations

import re

# "349", "349.5", "1,20,000.00" (Indian grouping) or "1,000,000" (western grouping).
_RUPEES = re.compile(r"(\d{1,3}(?:,\d{2,3})*|\d+)(?:\.(\d{1,2}))?")
_MAX_RUPEE_DIGITS = 13


def rupees_to_paise(text: str) -> int:
    """Parse a rupee amount without currency prefix, e.g. ``"1,20,000.50"`` -> ``12000050``.

    Raises ``ValueError`` unless the text is a non-negative amount with at most 2 decimals.
    """
    match = _RUPEES.fullmatch(text.strip())
    if match is None:
        raise ValueError("not a rupee amount")
    whole = match.group(1).replace(",", "")
    if len(whole) > _MAX_RUPEE_DIGITS:
        raise ValueError("amount too large")
    fraction = match.group(2)
    paise = int(fraction.ljust(2, "0")) if fraction else 0
    return int(whole) * 100 + paise
