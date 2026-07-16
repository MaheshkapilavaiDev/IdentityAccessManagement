CREATE TABLE refresh_tokens (

    id BIGINT NOT NULL AUTO_INCREMENT,

    token VARCHAR(500) NOT NULL,

    expiry_date DATETIME NOT NULL,

    revoked BOOLEAN DEFAULT FALSE,

    user_id BIGINT NOT NULL,

    PRIMARY KEY (id),

    CONSTRAINT uk_refresh_token
        UNIQUE (token),

    CONSTRAINT fk_refresh_token_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE

);