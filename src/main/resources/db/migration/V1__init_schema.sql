CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE zones (
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name        VARCHAR(255) NOT NULL,
    code        VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    created_at  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE buildings (
    id         UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    zone_id    UUID NOT NULL REFERENCES zones(id) ON DELETE CASCADE,
    name       VARCHAR(255) NOT NULL,
    code       VARCHAR(100) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (zone_id, code)
);

CREATE TABLE portfolios (
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name        VARCHAR(255) NOT NULL,
    description TEXT,
    created_at  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE apartments (
    id                    UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    unit_code             VARCHAR(100) NOT NULL UNIQUE,
    display_code          VARCHAR(100),
    area                  NUMERIC(10, 2),
    selling_price         NUMERIC(18, 2),
    tax                   NUMERIC(5, 2),
    status                VARCHAR(50)  NOT NULL DEFAULT 'DANG_BAN',
    furniture_description TEXT,
    building_id           UUID NOT NULL REFERENCES buildings(id) ON DELETE RESTRICT,
    created_at            TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at            TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE apartment_images (
    id           UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    apartment_id UUID NOT NULL REFERENCES apartments(id) ON DELETE CASCADE,
    url          TEXT NOT NULL,
    label        VARCHAR(255),
    sort_order   INT NOT NULL DEFAULT 0
);

CREATE TABLE users (
    id         UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    username   VARCHAR(100) NOT NULL UNIQUE,
    password   VARCHAR(255) NOT NULL,
    role       VARCHAR(50)  NOT NULL DEFAULT 'USER',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Default admin user (password: admin123)
INSERT INTO users (username, password, role)
VALUES ('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iKoOOYt.0EBx8jAUMaHVg4Rp.8Mu', 'ADMIN');

CREATE INDEX idx_apartments_building_id ON apartments(building_id);
CREATE INDEX idx_apartments_status ON apartments(status);
CREATE INDEX idx_buildings_zone_id ON buildings(zone_id);
CREATE INDEX idx_apartment_images_apartment_id ON apartment_images(apartment_id);