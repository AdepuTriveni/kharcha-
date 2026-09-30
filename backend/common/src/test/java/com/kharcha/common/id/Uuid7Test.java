package com.kharcha.common.id;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class Uuid7Test {

    @Test
    void hasVersion7AndRfcVariant() {
        UUID id = Uuid7.generate();
        assertThat(id.version()).isEqualTo(7);
        assertThat(id.variant()).isEqualTo(2);
    }

    @Test
    void embedsClockMillis() {
        Instant at = Instant.parse("2026-10-03T13:45:12.345Z");
        UUID id = Uuid7.generate(Clock.fixed(at, ZoneOffset.UTC));
        assertThat(Uuid7.timestamp(id)).isEqualTo(at);
    }

    @Test
    void laterMillisecondSortsAfter() {
        Instant t = Instant.parse("2026-10-03T13:45:12Z");
        UUID earlier = Uuid7.generate(Clock.fixed(t, ZoneOffset.UTC));
        UUID later = Uuid7.generate(Clock.fixed(t.plusMillis(1), ZoneOffset.UTC));
        // UUID.compareTo is signed; compare as strings, which sort like the unsigned bytes.
        assertThat(later.toString()).isGreaterThan(earlier.toString());
    }

    @Test
    void isUniqueWithinOneMillisecond() {
        Clock fixed = Clock.fixed(Instant.parse("2026-10-03T13:45:12Z"), ZoneOffset.UTC);
        Set<UUID> ids = new HashSet<>();
        for (int i = 0; i < 10_000; i++) {
            ids.add(Uuid7.generate(fixed));
        }
        assertThat(ids).hasSize(10_000);
    }

    @Test
    void timestampRejectsOtherVersions() {
        assertThatThrownBy(() -> Uuid7.timestamp(UUID.randomUUID()))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
