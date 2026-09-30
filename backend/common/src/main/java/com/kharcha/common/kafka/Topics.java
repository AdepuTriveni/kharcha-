package com.kharcha.common.kafka;

import java.util.List;

/**
 * Kafka topic names (spec §7.2). Every topic is keyed by {@code userId}.
 * Partitions and retention live in {@code infra/kafka/create-topics.sh}; TopicsTest keeps both in sync.
 */
public final class Topics {

    public static final String RAW_EVENTS = "raw-events";
    public static final String PARSED_TRANSACTIONS = "parsed-transactions";
    public static final String CLEAN_TRANSACTIONS = "clean-transactions";
    public static final String CASH_EVENTS = "cash-events";
    public static final String AGENT_TASKS = "agent-tasks";
    public static final String AGENT_RESULTS = "agent-results";
    public static final String USER_FEEDBACK = "user-feedback";
    public static final String NUDGE_OUTCOMES = "nudge-outcomes";
    public static final String MODEL_SHADOW = "model-shadow";

    public static final String DLT_SUFFIX = ".DLT";

    /** All main topics, in §7.2 order (dead-letter topics excluded). */
    public static final List<String> ALL = List.of(
            RAW_EVENTS, PARSED_TRANSACTIONS, CLEAN_TRANSACTIONS, CASH_EVENTS,
            AGENT_TASKS, AGENT_RESULTS, USER_FEEDBACK, NUDGE_OUTCOMES, MODEL_SHADOW);

    private Topics() {
    }

    /** Dead-letter topic used by the Spring Kafka dead-letter publisher. */
    public static String dlt(String topic) {
        return topic + DLT_SUFFIX;
    }
}
