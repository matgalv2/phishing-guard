CREATE TABLE IF NOT EXISTS subscriptions (
    msisdn      VARCHAR(32) PRIMARY KEY,
    status      VARCHAR(16) NOT NULL,
    updated_at  TIMESTAMPTZ NOT NULL DEFAULT now()
);