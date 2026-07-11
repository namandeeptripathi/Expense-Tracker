CREATE TABLE budgets (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,

                         monthly_limit DECIMAL(19,2) NOT NULL,

                         month INT NOT NULL,

                         year INT NOT NULL,

                         created_at TIMESTAMP NOT NULL,

                         updated_at TIMESTAMP NOT NULL,

                         user_id BIGINT NOT NULL,

                         CONSTRAINT fk_budget_user
                             FOREIGN KEY (user_id)
                                 REFERENCES users(id)
                                 ON DELETE CASCADE
);

CREATE INDEX idx_budget_user
    ON budgets(user_id);

CREATE UNIQUE INDEX uk_budget_user_month_year
    ON budgets(user_id, month, year);