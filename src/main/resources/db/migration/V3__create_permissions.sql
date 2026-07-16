CREATE TABLE permissions (

    id BIGINT NOT NULL AUTO_INCREMENT,

    name VARCHAR(255) NOT NULL,

    description VARCHAR(255),

    PRIMARY KEY (id),

    UNIQUE KEY uk_permission_name (name)

);