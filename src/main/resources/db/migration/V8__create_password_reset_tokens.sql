CREATE TABLE password_reset_tokens (

    id BIGINT NOT NULL AUTO_INCREMENT,

    token VARCHAR(255) NOT NULL,

    expiry_date DATETIME NOT NULL,

    used BOOLEAN DEFAULT FALSE,

    user_id BIGINT NOT NULL,

    PRIMARY KEY (id),

    CONSTRAINT uk_password_reset_token
        UNIQUE (token),

    CONSTRAINT fk_password_reset_token_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE

);