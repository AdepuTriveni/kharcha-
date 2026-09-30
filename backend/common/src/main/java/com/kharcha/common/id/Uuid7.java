package com.kharcha.common.id;

import java.security.SecureRandom;
import java.time.Clock;
import java.time.Instant;
import java.util.UUID;

/**
 * UUID version 7 (RFC 9562): 48-bit Unix-millisecond timestamp followed by random bits,
 * so IDs sort roughly by creation time. Used for {@code eventId} (§7.1).
 */
public final class Uuid7 {

    private static final SecureRandom RANDOM = new SecureRandom();

    private Uuid7() {
    }

    public static UUID generate() {
        return generate(Clock.systemUTC());
    }

    public static UUID generate(Clock clock) {
        long millis = clock.millis();
        long randA = RANDOM.nextInt(1 << 12);   // 12 random bits after the version nibble
        long randB = RANDOM.nextLong();         // 62 random bits after the variant
        long msb = ((millis & 0xFFFF_FFFF_FFFFL) << 16) | 0x7000L | randA;
        long lsb = (randB & 0x3FFF_FFFF_FFFF_FFFFL) | 0x8000_0000_0000_0000L;
        return new UUID(msb, lsb);
    }

    /** Creation time embedded in a UUIDv7. */
    public static Instant timestamp(UUID uuid) {
        if (uuid.version() != 7) {
            throw new IllegalArgumentException("not a UUIDv7: " + uuid);
        }
        return Instant.ofEpochMilli(uuid.getMostSignificantBits() >>> 16);
    }
}
