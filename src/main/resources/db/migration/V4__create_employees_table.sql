-- CREATE TABLE employees (
--     id SERIAL PRIMARY KEY,
--     full_name VARCHAR(100) NOT NULL CHECK (LENGTH(full_name) > 0),
--     hourly_rate NUMERIC(10, 2) NOT NULL CHECK (hourly_rate >= 0),
--     position INT NOT NULL REFERENCES employee_position_types(id),

--     -- for CASHIER
--     register_number INT UNIQUE,
--     shift_count INT DEFAULT 0,

--     -- for MANAGER
--     team_size INT DEFAULT 0
-- );
