--liquibase formatted sql

--changeset Andrey Antonov:1
--comment create table users
CREATE TABLE IF NOT EXISTS users
(
    id          BIGSERIAL PRIMARY KEY,
    first_name  VARCHAR(32)  NOT NULL,
    middle_name VARCHAR(32)  NOT NULL,
    last_name   VARCHAR(32)  NOT NULL,
    email       VARCHAR(32) NOT NULL UNIQUE,
    role        VARCHAR(16)  NOT NULL,
    enabled     BOOLEAN      NOT NULL,
    password    VARCHAR(256) NOT NULL
);

--changeset Andrey Antonov:2
--comment create table units
CREATE TABLE IF NOT EXISTS units
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(16) NOT NULL
);

--changeset Andrey Antonov:3
--comment create table products
CREATE TABLE IF NOT EXISTS products
(
    id      BIGSERIAL PRIMARY KEY,
    name    VARCHAR(64)                  NOT NULL,
    unit_id SERIAL REFERENCES units (id) NOT NULL,
    UNIQUE (name, unit_id)
);

--changeset Andrey Antonov:4
--comment create table manufactured_products
CREATE TABLE IF NOT EXISTS manufactured_products
(
    id         BIGSERIAL PRIMARY KEY,
    product_id BIGINT REFERENCES products (id) NOT NULL,
    user_id    BIGINT REFERENCES users (id)    NOT NULL,
    count      INTEGER                         NOT NULL,
    time       TIMESTAMP                       NOT NULL
);

--changeset Andrey Antonov:5
--comment create table working_norm
CREATE TABLE IF NOT EXISTS working_norms
(
    id            BIGSERIAL PRIMARY KEY,
    product_id    BIGINT REFERENCES products (id) NOT NULL,
    user_id       BIGINT REFERENCES users (id)    NOT NULL,
    working_count INTEGER                         NOT NULL,
    current_count INTEGER                         NOT NULL,
    deadline      TIMESTAMP                       NOT NULL,
    status        VARCHAR(16)                     NOT NULL
);
