CREATE TABLE categories (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    CONSTRAINT pk_categories PRIMARY KEY (id),
    CONSTRAINT uk_categories_name UNIQUE (name)
);

CREATE TABLE expenses (
    id BIGINT NOT NULL AUTO_INCREMENT,
    title VARCHAR(150) NOT NULL,
    amount DECIMAL(19, 2) NOT NULL,
    description VARCHAR(1000) NULL,
    expense_date DATE NOT NULL,
    payment_method VARCHAR(20) NOT NULL,
    category_id BIGINT NOT NULL,
    expense_type VARCHAR(20) NOT NULL,
    created_at TIMESTAMP(6) NOT NULL,
    updated_at TIMESTAMP(6) NOT NULL,
    user_id BIGINT NOT NULL,
    CONSTRAINT pk_expenses PRIMARY KEY (id),
    CONSTRAINT fk_expenses_category FOREIGN KEY (category_id) REFERENCES categories (id),
    CONSTRAINT fk_expenses_user FOREIGN KEY (user_id) REFERENCES users (id),
    INDEX idx_expenses_user_date (user_id, expense_date),
    INDEX idx_expenses_category_id (category_id)
);
