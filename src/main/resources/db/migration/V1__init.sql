CREATE TYPE CarState AS ENUM ('NEW', 'BROKEN', 'USED');

CREATE TABLE users
(
    id         BIGSERIAL PRIMARY KEY,
    email      VARCHAR(64) NOT NULL,
    telephone  VARCHAR(32) NOT NULL,
    created_at TIMESTAMP   NOT NULL,
    updated_at TIMESTAMP   NOT NULL
);

CREATE TABLE car_adds
(
    id            BIGSERIAL PRIMARY KEY,
    year          INT          NOT NULL,
    brand         VARCHAR(128) NOT NULL,
    model         VARCHAR(128) NOT NULL,
    engine_volume INT CHECK (engine_volume > 0),
    car_state     CarState     NOT NULL,
    mileage       INT CHECK (mileage >= 0),
    power         INT,
    user_id       BIGINT       NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    created_at    TIMESTAMP    NOT NULL,
    updated_at    TIMESTAMP    NOT NULL
);

CREATE TABLE add_telephones
(
    id        BIGSERIAL PRIMARY KEY,
    add_id    BIGINT REFERENCES car_adds (id) ON DELETE CASCADE,
    telephone VARCHAR(32) NOT NULL
);