-- Add updated_at to zones
ALTER TABLE zones ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP;
UPDATE zones SET updated_at = created_at WHERE updated_at IS NULL;

-- Add updated_at to buildings
ALTER TABLE buildings ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP;
UPDATE buildings SET updated_at = created_at WHERE updated_at IS NULL;

-- Add updated_at to portfolios
ALTER TABLE portfolios ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP;
UPDATE portfolios SET updated_at = created_at WHERE updated_at IS NULL;

-- Add updated_at to users
ALTER TABLE users ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP;
UPDATE users SET updated_at = created_at WHERE updated_at IS NULL;

-- Add created_at and updated_at to apartment_images (table has no timestamps)
ALTER TABLE apartment_images ADD COLUMN IF NOT EXISTS created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE apartment_images ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP;
UPDATE apartment_images SET updated_at = created_at WHERE updated_at IS NULL;

-- Add updated_at to apartment_status_history (append-only, will equal created_at)
ALTER TABLE apartment_status_history ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP;
UPDATE apartment_status_history SET updated_at = created_at WHERE updated_at IS NULL;

-- Add updated_at to audit_logs (append-only, will equal created_at)
ALTER TABLE audit_logs ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP;
UPDATE audit_logs SET updated_at = created_at WHERE updated_at IS NULL;