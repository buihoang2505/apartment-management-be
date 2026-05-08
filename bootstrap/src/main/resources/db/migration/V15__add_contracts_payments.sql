-- Contracts & payment schedules

CREATE TABLE contracts (
    id              UUID            NOT NULL DEFAULT gen_random_uuid() PRIMARY KEY,
    code            VARCHAR(50)     NOT NULL UNIQUE,
    type            VARCHAR(20)     NOT NULL,
    apartment_id    UUID            NOT NULL REFERENCES apartments(id) ON DELETE RESTRICT,
    customer_id     UUID            NOT NULL REFERENCES customers(id)  ON DELETE RESTRICT,
    start_date      DATE            NOT NULL,
    end_date        DATE,
    signed_date     DATE,
    total_amount    NUMERIC(18, 2)  NOT NULL,
    monthly_amount  NUMERIC(18, 2),
    deposit_amount  NUMERIC(18, 2),
    status          VARCHAR(20)     NOT NULL DEFAULT 'DRAFT',
    note            TEXT,
    created_at      TIMESTAMP       NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMP
);

CREATE INDEX idx_contracts_apartment_id ON contracts(apartment_id);
CREATE INDEX idx_contracts_customer_id  ON contracts(customer_id);
CREATE INDEX idx_contracts_status       ON contracts(status);
CREATE INDEX idx_contracts_type         ON contracts(type);

CREATE TABLE payment_schedules (
    id            UUID            NOT NULL DEFAULT gen_random_uuid() PRIMARY KEY,
    contract_id   UUID            NOT NULL REFERENCES contracts(id) ON DELETE CASCADE,
    period_index  INTEGER         NOT NULL,
    due_date      DATE            NOT NULL,
    amount        NUMERIC(18, 2)  NOT NULL,
    paid_amount   NUMERIC(18, 2),
    paid_date     DATE,
    status        VARCHAR(20)     NOT NULL DEFAULT 'PENDING',
    note          VARCHAR(500),
    created_at    TIMESTAMP       NOT NULL DEFAULT NOW(),
    updated_at    TIMESTAMP
);

CREATE INDEX idx_payment_schedules_contract_id ON payment_schedules(contract_id);
CREATE INDEX idx_payment_schedules_due_date    ON payment_schedules(due_date);
CREATE INDEX idx_payment_schedules_status      ON payment_schedules(status);
