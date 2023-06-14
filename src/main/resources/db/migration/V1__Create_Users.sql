CREATE TABLE escritura_user
(
    id         UUID NOT NULL,
    created    date,
    username   VARCHAR(255),
    password   VARCHAR(255),
    CONSTRAINT pk_escritura_user PRIMARY KEY (id)
);

CREATE TABLE user_role
(
    id      UUID NOT NULL,
    created date,
    role    INTEGER,
    user_id UUID,
    CONSTRAINT pk_user_role PRIMARY KEY (id)
);

ALTER TABLE user_role
    ADD CONSTRAINT FK_USER_ROLE_ON_USER FOREIGN KEY (user_id) REFERENCES escritura_user (id);

CREATE TABLE refresh_token
(
    id                    UUID NOT NULL,
    expiration_plus_hours BIGINT,
    expiration            TIMESTAMP WITHOUT TIME ZONE,
    code                  UUID,
    created               date,
    user_id               UUID,
    CONSTRAINT pk_refreshtoken PRIMARY KEY (id)
);

ALTER TABLE refresh_token
    ADD CONSTRAINT FK_REFRESHTOKEN_ON_USER FOREIGN KEY (user_id) REFERENCES escritura_user (id);

CREATE TABLE confirmation_token
(
    id                    UUID NOT NULL,
    expiration_plus_hours BIGINT,
    expiration            TIMESTAMP WITHOUT TIME ZONE,
    code                  UUID,
    created               date,
    user_id               UUID,
    CONSTRAINT pk_confirmationtoken PRIMARY KEY (id)
);

ALTER TABLE confirmation_token
    ADD CONSTRAINT FK_CONFIRMATIONTOKEN_ON_USER FOREIGN KEY (user_id) REFERENCES escritura_user (id);

CREATE TABLE access_token
(
    id                    UUID NOT NULL,
    expiration_plus_hours BIGINT,
    expiration            TIMESTAMP WITHOUT TIME ZONE,
    code                  UUID,
    created               date,
    user_id               UUID,
    CONSTRAINT pk_accesstoken PRIMARY KEY (id)
);

ALTER TABLE access_token
    ADD CONSTRAINT FK_ACCESSTOKEN_ON_USER FOREIGN KEY (user_id) REFERENCES escritura_user (id);
