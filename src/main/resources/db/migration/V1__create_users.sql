CREATE TABLE users (

    id BIGINT NOT NULL AUTO_INCREMENT,

    uuid VARCHAR(36) NOT NULL,

    first_name VARCHAR(255) NOT NULL,

    last_name VARCHAR(255) NOT NULL,

    email VARCHAR(255) NOT NULL,

    password VARCHAR(255) NOT NULL,

    phone VARCHAR(20),

    enabled BOOLEAN DEFAULT FALSE,

    locked BOOLEAN DEFAULT FALSE,

    failed_attempts INT DEFAULT 0,

    mfa_enabled BOOLEAN DEFAULT FALSE,

    email_verified BOOLEAN DEFAULT FALSE,

    created_at DATETIME,

    updated_at DATETIME,

    PRIMARY KEY (id),

    UNIQUE KEY uk_user_uuid (uuid),

    UNIQUE KEY uk_user_email (email),

    UNIQUE KEY uk_user_phone (phone)

);