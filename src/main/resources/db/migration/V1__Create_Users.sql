CREATE TABLE escritura_user
(
    id         UUID NOT NULL,
    created    date,
    email      VARCHAR(255),
    pw         VARCHAR(255),
    c_token_id UUID,
    CONSTRAINT pk_escriturauser PRIMARY KEY (id)
);

CREATE TABLE escritura_user_roles
(
    escritura_user_id UUID NOT NULL,
    roles_id          UUID NOT NULL,
    CONSTRAINT pk_escriturauser_roles PRIMARY KEY (escritura_user_id, roles_id)
);

CREATE TABLE user_role
(
    id      UUID NOT NULL,
    created date,
    role    INTEGER,
    CONSTRAINT pk_userrole PRIMARY KEY (id)
);

CREATE TABLE user_role_users
(
    user_role_id UUID NOT NULL,
    users_id     UUID NOT NULL,
    CONSTRAINT pk_userrole_users PRIMARY KEY (user_role_id, users_id)
);

ALTER TABLE user_role_users
    ADD CONSTRAINT fk_useroluse_on_escritura_user FOREIGN KEY (users_id) REFERENCES escritura_user (id);

ALTER TABLE user_role_users
    ADD CONSTRAINT fk_useroluse_on_user_role FOREIGN KEY (user_role_id) REFERENCES user_role (id);

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

ALTER TABLE escritura_user
    ADD CONSTRAINT FK_ESCRITURAUSER_ON_CTOKEN FOREIGN KEY (c_token_id) REFERENCES confirmation_token (id);

ALTER TABLE escritura_user_roles
    ADD CONSTRAINT fk_escuserol_on_escritura_user FOREIGN KEY (escritura_user_id) REFERENCES escritura_user (id);

ALTER TABLE escritura_user_roles
    ADD CONSTRAINT fk_escuserol_on_user_role FOREIGN KEY (roles_id) REFERENCES user_role (id);
