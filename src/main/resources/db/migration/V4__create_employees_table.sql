CREATE TABLE employees (
    id SERIAL PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL CHECK (LENGTH(full_name) > 0),
    hourly_rate NUMERIC(10, 2) NOT NULL CHECK (hourly_rate >= 0),
    position INT NOT NULL REFERENCES employee_position_types(id),
    isFullTime BOOLEAN DEFAULT TRUE,
    started_at DATE DEFAULT CURRENT_DATE,

    -- for CASHIER
    register_number INT UNIQUE,
    shift_count INT DEFAULT 0,

    -- for MANAGER
    team_size INT DEFAULT 0
);

INSERT INTO employees (full_name, hourly_rate, position, isFullTime, started_at, register_number, shift_count, team_size) VALUES
('Alice Johnson', 15.50, 1, TRUE, CURRENT_TIMESTAMP, 101, 5, NULL),
('Bob Smith', 18.00, 1, TRUE, CURRENT_TIMESTAMP, 102, 3, NULL),
('Charlie Brown', 25.00, 2, FALSE, CURRENT_TIMESTAMP, NULL, NULL, 10),
('Diana Prince', 30.00, 2, TRUE, CURRENT_TIMESTAMP, NULL, NULL, 15);