package com.kharcha.ingest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

/** Boots ingest-api against a real Postgres (same image as docker-compose) and checks Flyway V1. */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")
@Testcontainers
class SchemaMigrationIT {

    @Container
    static final PostgreSQLContainer POSTGRES = new PostgreSQLContainer("pgvector/pgvector:0.8.6-pg17");

    @DynamicPropertySource
    static void datasource(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES::getUsername);
        registry.add("spring.datasource.password", POSTGRES::getPassword);
    }

    @Autowired
    JdbcTemplate jdbc;

    @Test
    void createsAllCoreTables() {
        List<String> tables = jdbc.queryForList("""
                SELECT table_name FROM information_schema.tables
                WHERE table_schema = 'public' AND table_type = 'BASE TABLE'
                """, String.class);

        assertThat(tables).contains(
                "users", "devices", "raw_events", "processed_events", "parse_rules",
                "merchants", "merchant_aliases", "transactions", "transaction_sources",
                "cash_ledger", "budgets", "forecasts", "alerts_sent", "alert_feedback",
                "refund_cases");
    }

    @Test
    void enablesTrigramExtension() {
        List<String> extensions = jdbc.queryForList("SELECT extname FROM pg_extension", String.class);
        assertThat(extensions).contains("pg_trgm");
    }

    @Test
    void rejectsNonPositiveAmounts() {
        jdbc.update("INSERT INTO users (id) VALUES ('u_schema_it') ON CONFLICT DO NOTHING");

        assertThatThrownBy(() -> jdbc.update("""
                INSERT INTO transactions (id, user_id, amount_paise, direction, kind, status, channel,
                                          category, is_essential, txn_time, parse_method)
                VALUES ('t_zero', 'u_schema_it', 0, 'DEBIT', 'SPEND', 'SUCCESS', 'UPI',
                        'FOOD_DELIVERY', false, now(), 'RULE')
                """))
                .isInstanceOf(DataIntegrityViolationException.class);
    }
}
