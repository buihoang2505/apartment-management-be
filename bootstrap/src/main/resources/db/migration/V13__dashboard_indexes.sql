-- Dashboard read-model: indexes to support DashboardReadRepository.aggregate()
-- The query joins apartments -> buildings -> zones (via building_id, zone_id)
-- and filters/aggregates by status, apartment_type, and created_at.

-- 1) Composite for zone-filtered month windows: WHERE building_id = ? AND created_at >= ? AND created_at < ?
CREATE INDEX IF NOT EXISTS idx_apartments_building_created_at
    ON apartments (building_id, created_at);

-- 2) Status aggregation (SUM CASE WHEN status = ...)
CREATE INDEX IF NOT EXISTS idx_apartments_status
    ON apartments (status);

-- 3) Type aggregation (SUM CASE WHEN apartment_type = ...)
CREATE INDEX IF NOT EXISTS idx_apartments_apartment_type
    ON apartments (apartment_type);

-- 4) Zone join lookup on buildings (skip if already created by FK)
CREATE INDEX IF NOT EXISTS idx_buildings_zone_id
    ON buildings (zone_id);
