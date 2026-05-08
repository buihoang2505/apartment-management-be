CREATE TABLE IF NOT EXISTS bookings (
    id              UUID         NOT NULL DEFAULT gen_random_uuid() PRIMARY KEY,
    title           VARCHAR(255) NOT NULL,
    type            VARCHAR(20)  NOT NULL,
    status          VARCHAR(20)  NOT NULL DEFAULT 'SCHEDULED',
    start_time      TIMESTAMP    NOT NULL,
    end_time        TIMESTAMP    NOT NULL,
    location        VARCHAR(500),
    customer_id     UUID REFERENCES customers(id)  ON DELETE SET NULL,
    apartment_id    UUID REFERENCES apartments(id) ON DELETE SET NULL,
    assigned_to_id  UUID REFERENCES employees(id)  ON DELETE SET NULL,
    note            TEXT,
    created_at      TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_bookings_start_time      ON bookings(start_time);
CREATE INDEX IF NOT EXISTS idx_bookings_end_time        ON bookings(end_time);
CREATE INDEX IF NOT EXISTS idx_bookings_status          ON bookings(status);
CREATE INDEX IF NOT EXISTS idx_bookings_type            ON bookings(type);
CREATE INDEX IF NOT EXISTS idx_bookings_customer_id     ON bookings(customer_id);
CREATE INDEX IF NOT EXISTS idx_bookings_apartment_id    ON bookings(apartment_id);
CREATE INDEX IF NOT EXISTS idx_bookings_assigned_to_id  ON bookings(assigned_to_id);
