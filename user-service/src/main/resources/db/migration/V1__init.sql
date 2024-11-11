CREATE TABLE user
(
    id                BIGINT AUTO_INCREMENT NOT NULL,
    username          VARCHAR(255) NULL,
    email             VARCHAR(255) NULL,
    password          VARCHAR(255) NULL,
    enabled           BIT(1) NOT NULL,
    verification_code VARCHAR(64) NULL,
    `role`            VARCHAR(255) NULL,
    CONSTRAINT pk_user PRIMARY KEY (id)
);

ALTER TABLE user
    ADD CONSTRAINT uc_user_email UNIQUE (email);

ALTER TABLE user
    ADD CONSTRAINT uc_user_username UNIQUE (username);
