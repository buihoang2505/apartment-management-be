-- Ensure default manager user exists (password: manager123)
-- Using UPSERT so it's safe to re-run
INSERT INTO users (username, password, role, full_name, is_active)
VALUES (
    'manager',
    '$2a$10$/FZi2VqSMYZJaESPLJIVO.uSt27lFAz4KlhIqks0SDxyaxd0kpmyu',
    'MANAGER',
    'Default Manager',
    true
)
ON CONFLICT (username) DO UPDATE
    SET password  = EXCLUDED.password,
        is_active = true;
