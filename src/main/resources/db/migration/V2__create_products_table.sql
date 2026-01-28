CREATE TABLE products (
    id SERIAL PRIMARY KEY,
    product_type_id INT NOT NULL REFERENCES product_types(id), -- 'FRESH', 'FROZEN'
    name VARCHAR(255) NOT NULL CHECK (LENGTH(name) > 0),
    quantity DECIMAL(10, 2) NOT NULL CHECK (quantity >= 0),
    unitPrice DECIMAL(10, 2) NOT NULL CHECK (unitPrice >= 0),
    category VARCHAR(100) NOT NULL CHECK (LENGTH(category) > 0),
    isDiscontinued BOOLEAN DEFAULT FALSE NOT NULL,
    storageTemp DECIMAL(5,2) -- for FROZEN
);

INSERT INTO products (product_type_id, name, quantity, unitPrice, category, isDiscontinued, storageTemp) VALUES
(1, 'Bananas', 100.00, 0.50, 'Fruits', FALSE, NULL),
(1, 'Apples', 150.00, 0.70, 'Fruits', TRUE, NULL),
(2, 'Frozen Peas', 200.00, 1.20, 'Vegetables', FALSE, -18.00),
(2, 'Ice Cream', 80.00, 3.50, 'Desserts', TRUE, -20.00);