package com.kharcha.common.event;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * Envelope shared by every Kafka topic (spec §7.1). The Kafka key is always {@code userId}.
 * Changing a payload's shape means bumping {@code schemaVersion} and updating §7.
 *
 * @param eventId     UUIDv7; generated on the phone for raw events
 * @param causationId {@code eventId} of the event that caused this one, or {@code null}
 * @param <T>         payload type (§7.3)
 */
public record EventEnvelope<T>(
        UUID eventId,
        String userId,
        String type,
        int schemaVersion,
        Instant occurredAt,
        Instant producedAt,
        String producer,
        UUID causationId,
        T payload) {

    public EventEnvelope {
        Objects.requireNonNull(eventId, "eventId");
        requireNonBlank(userId, "userId");
        requireNonBlank(type, "type");
        if (schemaVersion < 1) {
            throw new IllegalArgumentException("schemaVersion must be >= 1, was " + schemaVersion);
        }
        Objects.requireNonNull(occurredAt, "occurredAt");
        Objects.requireNonNull(producedAt, "producedAt");
        requireNonBlank(producer, "producer");
        Objects.requireNonNull(payload, "payload");
    }

    /**
     * Builds the next event in a chain (e.g. raw event → parsed transaction): same user and
     * {@code occurredAt}, with {@code causationId} pointing at this event.
     */
    public <R> EventEnvelope<R> next(UUID nextEventId, String nextType, int nextSchemaVersion,
                                     Instant now, String nextProducer, R nextPayload) {
        return new EventEnvelope<>(nextEventId, userId, nextType, nextSchemaVersion,
                occurredAt, now, nextProducer, eventId, nextPayload);
    }

    private static void requireNonBlank(String value, String name) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(name + " must not be blank");
        }
    }
}
