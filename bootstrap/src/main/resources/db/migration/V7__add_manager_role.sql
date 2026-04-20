-- Seed default manager user (password: manager123)
INSERT INTO users (username, password, role, full_name, is_active)
VALUES (
    'manager',
    '$2a$10$7YnxZR2JXiTj2sARHvJgUe3ycE4pJCZgMLwHJm/qpCZI.yFf5Pmxy',
    'MANAGER',
    'Default Manager',
    true
)
ON CONFLICT (username) DO NOTHING;

-- Block any existing USER-role accounts from logging in (set inactive)
UPDATE users SET is_active = false WHERE role NOT IN ('ADMIN', 'MANAGER');
