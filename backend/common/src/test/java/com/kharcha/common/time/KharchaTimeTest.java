package com.kharcha.common.time;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class KharchaTimeTest {

    @Test
    void utcEveningIsNextDayInIst() {
        // 20:00 UTC = 01:30 IST the next day
        Instant instant = Instant.parse("2026-10-03T20:00:00Z");
        assertThat(KharchaTime.istDate(instant)).isEqualTo(LocalDate.of(2026, 10, 4));
        assertThat(KharchaTime.toIst(instant).getHour()).isEqualTo(1);
        assertThat(KharchaTime.toIst(instant).getMinute()).isEqualTo(30);
    }

    @Test
    void startOfIstDayIsPreviousEveningUtc() {
        assertThat(KharchaTime.startOfIstDay(LocalDate.of(2026, 10, 4)))
                .isEqualTo(Instant.parse("2026-10-03T18:30:00Z"));
    }
}
