CREATE TABLE incomes (
    id BIGINT NOT NULL AUTO_INCREMENT,
    source VARCHAR(150) NOT NULL,
    amount DECIMAL(19, 2) NOT NULL,
    description VARCHAR(1000) NULL,
    income_date DATE NOT NULL,
    income_type VARCHAR(20) NOT NULL,
    created_at TIMESTAMP(6) NOT NULL,
    updated_at TIMESTAMP(6) NOT NULL,
    user_id BIGINT NOT NULL,
    CONSTRAINT pk_incomes PRIMARY KEY (id),
    CONSTRAINT fk_incomes_user FOREIGN KEY (user_id) REFERENCES users (id),
    INDEX idx_incomes_user_date (user_id, income_date),
    INDEX idx_incomes_user_type (user_id, income_type)
);
