CREATE TABLE productos (
                           id SERIAL PRIMARY KEY,
                           title VARCHAR(255) NOT NULL,
                           description TEXT,
                           precio DECIMAL(10, 2) NOT NULL
);