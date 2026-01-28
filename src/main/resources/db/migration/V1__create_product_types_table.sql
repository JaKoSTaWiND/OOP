CREATE TABLE product_types (
    id SERIAL PRIMARY KEY,
    product_type VARCHAR(100) NOT NULL UNIQUE -- 'FRESH', 'FROZEN'
);

INSERT INTO product_types (product_type) VALUES ('FRESH'), ('FROZEN');