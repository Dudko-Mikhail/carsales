CREATE TYPE CarState AS ENUM ('NEW', 'BROKEN', 'USED');

CREATE TABLE users
(
    id         BIGSERIAL PRIMARY KEY,
    email      VARCHAR(64) NOT NULL,
    telephone  VARCHAR(32) NOT NULL,
    created_at TIMESTAMP   NOT NULL,
    updated_at TIMESTAMP   NOT NULL
);

CREATE TABLE car_ads
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

CREATE TABLE ad_telephones
(
    id        BIGSERIAL PRIMARY KEY,
    ad_id    BIGINT REFERENCES car_ads (id) ON DELETE CASCADE,
    telephone VARCHAR(32) NOT NULL
);