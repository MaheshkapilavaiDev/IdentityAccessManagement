CREATE TABLE otp (

    id BIGINT NOT NULL AUTO_INCREMENT,

    code VARCHAR(20) NOT NULL,

    expiry_time DATETIME NOT NULL,

    verified BOOLEAN DEFAULT FALSE,

    user_id BIGINT NOT NULL,

    PRIMARY KEY (id),

    CONSTRAINT fk_otp_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE

);