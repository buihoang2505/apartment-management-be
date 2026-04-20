-- Departments table
CREATE TABLE departments (
    id          UUID         NOT NULL DEFAULT gen_random_uuid() PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    code        VARCHAR(50)  NOT NULL UNIQUE,
    description TEXT,
    created_at  TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMP
);

-- Employees table
CREATE TABLE employees (
    id            UUID         NOT NULL DEFAULT gen_random_uuid() PRIMARY KEY,
    full_name     VARCHAR(255) NOT NULL,
    email         VARCHAR(255) NOT NULL UNIQUE,
    phone         VARCHAR(20),
    position      VARCHAR(255),
    department_id UUID         REFERENCES departments(id) ON DELETE SET NULL,
    status        VARCHAR(20)  NOT NULL DEFAULT 'ACTIVE',
    created_at    TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at    TIMESTAMP
);

CREATE INDEX idx_employees_department_id ON employees(department_id);
CREATE INDEX idx_employees_status        ON employees(status);
