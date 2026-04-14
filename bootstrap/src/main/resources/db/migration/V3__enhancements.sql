-- Buildings enhancements
ALTER TABLE buildings
    ADD COLUMN IF NOT EXISTS type         VARCHAR(50),
    ADD COLUMN IF NOT EXISTS total_floors INT,
    ADD COLUMN IF NOT EXISTS description  TEXT;

-- Apartments enhancements
ALTER TABLE apartments
    ADD COLUMN IF NOT EXISTS apartment_type VARCHAR(50),
    ADD COLUMN IF NOT EXISTS floor_number   INT,
    ADD COLUMN IF NOT EXISTS direction      VARCHAR(50),
    ADD COLUMN IF NOT EXISTS bedroom_count  INT;

-- Users enhancements
ALTER TABLE users
    ADD COLUMN IF NOT EXISTS full_name VARCHAR(255),
    ADD COLUMN IF NOT EXISTS email     VARCHAR(255),
    ADD COLUMN IF NOT EXISTS phone     VARCHAR(20),
    ADD COLUMN IF NOT EXISTS is_active BOOLEAN NOT NULL DEFAULT true;

-- Portfolio-zones many-to-many
CREATE TABLE IF NOT EXISTS portfolio_zones (
    portfolio_id UUID NOT NULL REFERENCES portfolios(id) ON DELETE CASCADE,
    zone_id      UUID NOT NULL REFERENCES zones(id)      ON DELETE CASCADE,
    PRIMARY KEY (portfolio_id, zone_id)
);

-- Apartment status history
CREATE TABLE IF NOT EXISTS apartment_status_history (
    id           UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    apartment_id UUID         NOT NULL REFERENCES apartments(id) ON DELETE CASCADE,
    old_status   VARCHAR(50),
    new_status   VARCHAR(50)  NOT NULL,
    changed_by   UUID         REFERENCES users(id) ON DELETE SET NULL,
    note         TEXT,
    created_at   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Audit logs
CREATE TABLE IF NOT EXISTS audit_logs (
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id     UUID        REFERENCES users(id) ON DELETE SET NULL,
    action      VARCHAR(100) NOT NULL,
    entity_type VARCHAR(100),
    entity_id   VARCHAR(255),
    old_value   TEXT,
    new_value   TEXT,
    ip_address  VARCHAR(45),
    created_at  TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_apartment_status_history_apartment_id ON apartment_status_history(apartment_id);
CREATE INDEX IF NOT EXISTS idx_audit_logs_user_id    ON audit_logs(user_id);
CREATE INDEX IF NOT EXISTS idx_audit_logs_entity     ON audit_logs(entity_type, entity_id);
CREATE INDEX IF NOT EXISTS idx_audit_logs_created_at ON audit_logs(created_at);