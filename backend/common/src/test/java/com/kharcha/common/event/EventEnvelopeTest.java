package com.kharcha.common.event;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.kharcha.common.id.Uuid7;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class EventEnvelopeTest {

    private static final Instant OCCURRED = Instant.parse("2026-10-03T13:45:12Z");
    private static final Instant PRODUCED = Instant.parse("2026-10-03T13:45:20Z");

    private static EventEnvelope<Map<String, String>> raw() {
        return new EventEnvelope<>(Uuid7.generate(), "u_8f2k1", "RAW_NOTIFICATION", 1,
                OCCURRED, PRODUCED, "ingest-api", null, Map.of("sender", "AX-HDFCBK"));
    }

    @Test
    void nextKeepsUserAndOccurredAtAndLinksCausation() {
        EventEnvelope<Map<String, String>> raw = raw();
        Instant now = PRODUCED.plusSeconds(1);
        UUID nextId = Uuid7.generate();

        EventEnvelope<String> parsed = raw.next(nextId, "PARSED_TRANSACTION", 1, now, "processor", "p");

        assertThat(parsed.eventId()).isEqualTo(nextId);
        assertThat(parsed.userId()).isEqualTo(raw.userId());
        assertThat(parsed.occurredAt()).isEqualTo(OCCURRED);
        assertThat(parsed.producedAt()).isEqualTo(now);
        assertThat(parsed.causationId()).isEqualTo(raw.eventId());
        assertThat(parsed.producer()).isEqualTo("processor");
    }

    @Test
    void rejectsBlankUserId() {
        assertThatThrownBy(() -> new EventEnvelope<>(Uuid7.generate(), " ", "RAW_SMS", 1,
                OCCURRED, PRODUCED, "ingest-api", null, "x"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("userId");
    }

    @Test
    void rejectsSchemaVersionBelowOne() {
        assertThatThrownBy(() -> new EventEnvelope<>(Uuid7.generate(), "u_1", "RAW_SMS", 0,
                OCCURRED, PRODUCED, "ingest-api", null, "x"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("schemaVersion");
    }

    @Test
    void rejectsMissingPayload() {
        assertThatThrownBy(() -> new EventEnvelope<>(Uuid7.generate(), "u_1", "RAW_SMS", 1,
                OCCURRED, PRODUCED, "ingest-api", null, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("payload");
    }
}
