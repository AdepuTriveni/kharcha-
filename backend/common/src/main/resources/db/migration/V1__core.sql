-- Kharcha core schema (spec §8). Copied verbatim from PROJECT_SPEC.md; change via new migrations only.
CREATE EXTENSION IF NOT EXISTS pg_trgm;

CREATE TABLE users (
  id TEXT PRIMARY KEY, firebase_uid TEXT UNIQUE, display_name TEXT,
  timezone TEXT NOT NULL DEFAULT 'Asia/Kolkata',
  roast_level TEXT NOT NULL DEFAULT 'MEDIUM',          -- OFF|MILD|MEDIUM|SAVAGE
  telegram_chat_id BIGINT UNIQUE,
  experiment_group TEXT,                               -- BANDIT|PLAIN|NULL
  ml_consent BOOLEAN NOT NULL DEFAULT false,           -- allow redacted data for training
  quiet_start TIME NOT NULL DEFAULT '22:00', quiet_end TIME NOT NULL DEFAULT '08:00',
  created_at TIMESTAMPTZ NOT NULL DEFAULT now()
);
CREATE TABLE devices (id TEXT PRIMARY KEY, user_id TEXT NOT NULL REFERENCES users(id),
  fcm_token TEXT, app_version TEXT, device_model TEXT, on_device_llm BOOLEAN DEFAULT false,
  created_at TIMESTAMPTZ NOT NULL DEFAULT now());

CREATE TABLE raw_events (event_id UUID PRIMARY KEY, user_id TEXT NOT NULL REFERENCES users(id),
  type TEXT NOT NULL, source_app TEXT, sender TEXT, title TEXT, text TEXT,
  device_parse JSONB, posted_at TIMESTAMPTZ NOT NULL, received_at TIMESTAMPTZ NOT NULL DEFAULT now());
CREATE INDEX ON raw_events (user_id, posted_at);

CREATE TABLE processed_events (consumer TEXT NOT NULL, event_id UUID NOT NULL,
  processed_at TIMESTAMPTZ NOT NULL DEFAULT now(), PRIMARY KEY (consumer, event_id));

CREATE TABLE parse_rules (id TEXT PRIMARY KEY, sender_pattern TEXT, regex TEXT NOT NULL,
  field_map JSONB NOT NULL, status TEXT NOT NULL,     -- CANDIDATE|ACTIVE|DISABLED
  origin TEXT NOT NULL, match_count INT NOT NULL DEFAULT 0, mismatch_count INT NOT NULL DEFAULT 0,
  created_at TIMESTAMPTZ NOT NULL DEFAULT now());

CREATE TABLE merchants (id TEXT PRIMARY KEY, name TEXT NOT NULL, default_category TEXT NOT NULL);
CREATE TABLE merchant_aliases (alias TEXT PRIMARY KEY, merchant_id TEXT NOT NULL REFERENCES merchants(id),
  source TEXT NOT NULL);                               -- SEED|RULE|LLM|USER
CREATE INDEX ON merchant_aliases USING gin (alias gin_trgm_ops);

CREATE TABLE transactions (
  id TEXT PRIMARY KEY, user_id TEXT NOT NULL REFERENCES users(id),
  amount_paise BIGINT NOT NULL CHECK (amount_paise > 0),
  direction TEXT NOT NULL, kind TEXT NOT NULL, status TEXT NOT NULL, channel TEXT NOT NULL,
  merchant_id TEXT REFERENCES merchants(id), merchant_raw TEXT,
  category TEXT NOT NULL, is_essential BOOLEAN NOT NULL,
  reference_id TEXT, account_hint TEXT, balance_after_paise BIGINT,
  txn_time TIMESTAMPTZ NOT NULL, parse_method TEXT NOT NULL, user_corrected BOOLEAN NOT NULL DEFAULT false,
  version INT NOT NULL DEFAULT 1,
  created_at TIMESTAMPTZ NOT NULL DEFAULT now(), updated_at TIMESTAMPTZ NOT NULL DEFAULT now());
CREATE INDEX ON transactions (user_id, txn_time);
CREATE INDEX ON transactions (user_id, reference_id);
CREATE INDEX ON transactions (user_id, amount_paise, txn_time);

CREATE TABLE transaction_sources (transaction_id TEXT NOT NULL REFERENCES transactions(id),
  raw_event_id UUID NOT NULL REFERENCES raw_events(event_id), PRIMARY KEY (transaction_id, raw_event_id));

CREATE TABLE cash_ledger (id TEXT PRIMARY KEY, user_id TEXT NOT NULL REFERENCES users(id),
  entry_type TEXT NOT NULL, amount_paise BIGINT NOT NULL CHECK (amount_paise > 0),
  category TEXT, note TEXT, related_transaction_id TEXT REFERENCES transactions(id),
  occurred_at TIMESTAMPTZ NOT NULL, prompted BOOLEAN NOT NULL DEFAULT false);
CREATE INDEX ON cash_ledger (user_id, occurred_at);

CREATE TABLE budgets (user_id TEXT NOT NULL REFERENCES users(id), category TEXT NOT NULL,
  monthly_limit_paise BIGINT NOT NULL, PRIMARY KEY (user_id, category));

CREATE TABLE forecasts (id TEXT PRIMARY KEY, user_id TEXT NOT NULL REFERENCES users(id),
  computed_at TIMESTAMPTZ NOT NULL, balance_now_paise BIGINT NOT NULL,
  broke_p20 DATE, broke_p50 DATE, broke_p80 DATE, horizon_days INT NOT NULL, inputs JSONB NOT NULL);

CREATE TABLE alerts_sent (id TEXT PRIMARY KEY, user_id TEXT NOT NULL REFERENCES users(id),
  alert_type TEXT NOT NULL, text TEXT NOT NULL, dedupe_key TEXT, agent_run_id TEXT,
  nudge_decision_id TEXT, sent_at TIMESTAMPTZ, status TEXT NOT NULL); -- QUEUED|SENT|SUPPRESSED|FAILED
CREATE UNIQUE INDEX ON alerts_sent (user_id, dedupe_key) WHERE dedupe_key IS NOT NULL;
CREATE TABLE alert_feedback (alert_id TEXT NOT NULL REFERENCES alerts_sent(id), reaction TEXT NOT NULL,
  created_at TIMESTAMPTZ NOT NULL DEFAULT now(), PRIMARY KEY (alert_id, reaction));

CREATE TABLE refund_cases (id TEXT PRIMARY KEY, user_id TEXT NOT NULL REFERENCES users(id),
  case_type TEXT NOT NULL, transaction_id TEXT NOT NULL REFERENCES transactions(id),
  amount_paise BIGINT NOT NULL, reference_id TEXT, opened_at TIMESTAMPTZ NOT NULL,
  deadline_at TIMESTAMPTZ NOT NULL, state TEXT NOT NULL, reversed_at TIMESTAMPTZ,
  compensation_owed_paise BIGINT DEFAULT 0, compensation_received_paise BIGINT DEFAULT 0,
  workflow_id TEXT UNIQUE, complaint_draft TEXT, updated_at TIMESTAMPTZ NOT NULL DEFAULT now());
