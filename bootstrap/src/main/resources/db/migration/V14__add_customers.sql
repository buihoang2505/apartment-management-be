-- Customers / Leads table
CREATE TABLE customers (
    id                       UUID         NOT NULL DEFAULT gen_random_uuid() PRIMARY KEY,
    full_name                VARCHAR(255) NOT NULL,
    email                    VARCHAR(255),
    phone                    VARCHAR(20)  NOT NULL,
    source                   VARCHAR(20)  NOT NULL DEFAULT 'OTHER',
    status                   VARCHAR(20)  NOT NULL DEFAULT 'NEW',
    assigned_to_id           UUID         REFERENCES employees(id) ON DELETE SET NULL,
    interested_apartment_id  UUID         REFERENCES apartments(id) ON DELETE SET NULL,
    note                     TEXT,
    created_at               TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at               TIMESTAMP
);

CREATE INDEX idx_customers_status              ON customers(status);
CREATE INDEX idx_customers_source              ON customers(source);
CREATE INDEX idx_customers_assigned_to_id      ON customers(assigned_to_id);
CREATE INDEX idx_customers_interested_apartment ON customers(interested_apartment_id);
CREATE INDEX idx_customers_phone               ON customers(phone);
CREATE INDEX idx_customers_created_at          ON customers(created_at DESC);
