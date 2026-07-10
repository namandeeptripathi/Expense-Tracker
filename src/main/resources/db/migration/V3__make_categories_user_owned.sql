ALTER TABLE categories DROP INDEX uk_categories_name;

ALTER TABLE categories
    MODIFY COLUMN name VARCHAR(50) NOT NULL,
    ADD COLUMN description VARCHAR(500) NULL AFTER name,
    ADD COLUMN color VARCHAR(20) NULL AFTER description,
    ADD COLUMN icon VARCHAR(64) NULL AFTER color,
    ADD COLUMN created_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) AFTER icon,
    ADD COLUMN updated_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) AFTER created_at,
    ADD COLUMN user_id BIGINT NOT NULL AFTER updated_at,
    ADD CONSTRAINT fk_categories_user FOREIGN KEY (user_id) REFERENCES users (id),
    ADD CONSTRAINT uk_categories_user_name UNIQUE (user_id, name),
    ADD INDEX idx_categories_user_name (user_id, name);
