--liquibase formatted sql

--changeset Andrey Antonov:1
--comment adding a farm owner
INSERT INTO users(first_name, middle_name, last_name, email, "role", enabled, "password")
VALUES ('Andrey', 'Antonov', 'Andreevich', 'andreyantonov2702@gmail.com', 'ROLE_OWNER', true,
        '$2y$10$jxBQCFmdCW.9q1uI3h6NheEygO3dbZiIWw7LXo8EWEj14ZL.iJJzC');

--changeset Andrey Antonov:2
--comment adding initial units to get started
INSERT INTO units(name)
VALUES ('кг'),
       ('шт'),
       ('л');

--changeset Andrey Antonov:3
--comment adding initial products to get started
INSERT INTO products(name, unit_id)
VALUES ('Яйки куриные', (SELECT id FROM units WHERE name = 'шт')),
       ('Яблоки', (SELECT id FROM units WHERE name = 'шт')),
       ('Молоко', (SELECT id FROM units WHERE name = 'л'))