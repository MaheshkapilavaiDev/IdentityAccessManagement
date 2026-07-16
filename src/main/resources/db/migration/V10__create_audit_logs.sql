CREATE TABLE audit_logs (

    id BIGINT NOT NULL AUTO_INCREMENT,

    action VARCHAR(255),

    timestamp DATETIME,

    ip_address VARCHAR(255),

    description VARCHAR(500),

    created_at DATETIME,

    device_name VARCHAR(255),

    user_id BIGINT,

    PRIMARY KEY (id),

    CONSTRAINT fk_audit_log_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE SET NULL

);