CREATE TABLE sessions (
    id BIGINT NOT NULL AUTO_INCREMENT,
    token VARCHAR(500),
    login_time DATETIME,
    logout_time DATETIME,
    active BOOLEAN DEFAULT TRUE,
    device_name VARCHAR(255),
    ip_address VARCHAR(255),
    user_id BIGINT,
    PRIMARY KEY (id),
    CONSTRAINT fk_user_session_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE
);