CREATE TABLE customers(
    id SERIAL PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL CHECK (LENGTH(full_name) > 0),
    phone VARCHAR(12) NOT NULL CHECK (LENGTH(phone) BETWEEN 11 AND 12),
    loyalty_points INT NOT NULL CHECK (loyalty_points >= 0) DEFAULT 0,
    is_vip BOOLEAN DEFAULT FALSE
);

INSERT INTO customers (full_name, phone, loyalty_points, is_vip) VALUES 
('John Doe', '15551234567', 150, FALSE),
('Alice Johnson', '+14155552677', 1200, TRUE),
('Robert Smith', '18005550199', 0, FALSE),
('Emily Brown', '+44207123456', 500, TRUE),
('Michael Wilson', '16505550123', 25, FALSE);