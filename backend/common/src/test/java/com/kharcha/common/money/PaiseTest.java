package com.kharcha.common.money;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class PaiseTest {

    @ParameterizedTest
    @CsvSource(delimiter = '|', value = {
            "349          | 34900",
            "349.00       | 34900",
            "349.5        | 34950",
            "0.01         | 1",
            "' 10 '       | 1000",
            "1,20,000.00  | 12000000",
            "1,000,000    | 100000000",
            "120000       | 12000000",
    })
    void parsesRupees(String text, long expectedPaise) {
        assertThat(Paise.fromRupees(text)).isEqualTo(expectedPaise);
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "abc", "-349", "349.123", "1,2", "Rs.349", "349.", ".50", "12345678901234"})
    void rejectsInvalidAmounts(String text) {
        assertThatThrownBy(() -> Paise.fromRupees(text)).isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @CsvSource(delimiter = '|', value = {
            "0            | ₹0.00",
            "5            | ₹0.05",
            "34900        | ₹349.00",
            "100000       | ₹1,000.00",
            "12345678     | ₹1,23,456.78",
            "123456789000 | ₹1,23,45,67,890.00",
            "-34950       | -₹349.50",
    })
    void formatsWithIndianGrouping(long paise, String expected) {
        assertThat(Paise.format(paise)).isEqualTo(expected);
    }
}
