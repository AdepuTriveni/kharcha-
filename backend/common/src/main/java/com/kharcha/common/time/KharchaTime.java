package com.kharcha.common.time;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/** Store UTC ({@link Instant}), display IST (CLAUDE.md rule 2). */
public final class KharchaTime {

    public static final ZoneId IST = ZoneId.of("Asia/Kolkata");

    private KharchaTime() {
    }

    public static ZonedDateTime toIst(Instant instant) {
        return instant.atZone(IST);
    }

    /** Calendar date in India, e.g. for "today's spend" and weekly reviews. */
    public static LocalDate istDate(Instant instant) {
        return toIst(instant).toLocalDate();
    }

    /** First instant of an IST calendar day, for date-range queries on UTC columns. */
    public static Instant startOfIstDay(LocalDate date) {
        return date.atStartOfDay(IST).toInstant();
    }
}
