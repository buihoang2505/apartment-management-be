ALTER TABLE payment_schedules
    ADD COLUMN IF NOT EXISTS category VARCHAR(20) NOT NULL DEFAULT 'INSTALLMENT';

CREATE INDEX IF NOT EXISTS idx_payment_schedules_category ON payment_schedules(category);
