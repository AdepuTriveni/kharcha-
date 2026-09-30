package com.kharcha.common.money;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Money is always {@code long} paise (CLAUDE.md rule 1). Never use float or double for money.
 */
public final class Paise {

    /** "349", "349.5", "1,20,000.00" (Indian grouping) or "1,000,000" (western grouping). */
    private static final Pattern RUPEES = Pattern.compile("(\\d{1,3}(?:,\\d{2,3})*|\\d+)(?:\\.(\\d{1,2}))?");
    private static final int MAX_RUPEE_DIGITS = 13;
    private static final String RUPEE_SIGN = "₹";

    private Paise() {
    }

    /**
     * Parses a rupee amount without currency prefix, e.g. {@code "1,20,000.50"} → {@code 12000050}.
     *
     * @throws IllegalArgumentException if the text is not a non-negative amount with at most 2 decimals
     */
    public static long fromRupees(String text) {
        if (text == null) {
            throw new IllegalArgumentException("amount must not be null");
        }
        Matcher m = RUPEES.matcher(text.strip());
        if (!m.matches()) {
            throw new IllegalArgumentException("not a rupee amount");
        }
        String whole = m.group(1).replace(",", "");
        if (whole.length() > MAX_RUPEE_DIGITS) {
            throw new IllegalArgumentException("amount too large");
        }
        String fraction = m.group(2);
        long paise = 0;
        if (fraction != null) {
            paise = Long.parseLong(fraction) * (fraction.length() == 1 ? 10 : 1);
        }
        return Math.addExact(Math.multiplyExact(Long.parseLong(whole), 100L), paise);
    }

    /** Formats for display with Indian digit grouping, e.g. {@code 12345678} → {@code "₹1,23,456.78"}. */
    public static String format(long paise) {
        long abs = Math.absExact(paise);
        String rupees = Long.toString(abs / 100);
        long rest = abs % 100;

        StringBuilder grouped = new StringBuilder();
        int len = rupees.length();
        if (len <= 3) {
            grouped.append(rupees);
        } else {
            String head = rupees.substring(0, len - 3);
            int first = head.length() % 2;
            if (first == 1) {
                grouped.append(head, 0, 1);
            }
            for (int i = first; i < head.length(); i += 2) {
                if (!grouped.isEmpty()) {
                    grouped.append(',');
                }
                grouped.append(head, i, i + 2);
            }
            grouped.append(',').append(rupees, len - 3, len);
        }
        return (paise < 0 ? "-" : "") + RUPEE_SIGN + grouped + "." + (rest < 10 ? "0" : "") + rest;
    }
}
