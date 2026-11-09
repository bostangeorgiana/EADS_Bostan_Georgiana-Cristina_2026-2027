

CREATE TABLE users (
                       id       BIGSERIAL PRIMARY KEY,
                       username VARCHAR(100) NOT NULL UNIQUE,
                       password VARCHAR(100) NOT NULL,
                       role     VARCHAR(20)  NOT NULL CHECK (role IN ('ADMIN', 'CUSTOMER', 'DRIVER'))
);

ALTER TABLE customers ADD COLUMN user_id BIGINT UNIQUE REFERENCES users(id);
ALTER TABLE drivers   ADD COLUMN user_id BIGINT UNIQUE REFERENCES users(id);