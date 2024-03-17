INSERT INTO users(first_name, middle_name, last_name, email, "role", enabled, "password")
VALUES ('andrey', 'antonov', 'andreevich', 'email1', 'ROLE_EMPLOYEE', true,
        '$2y$10$gh9C1nnMgokphEgYM/m2ke9lB1I3f8XrKmYHM3c9AQJdgmyYvWAv6'),
       ('ivan', 'ivanov', 'ivanovich', 'email2', 'ROLE_EMPLOYEE', true,
        '$2y$10$gh9C1nnMgokphEgYM/m2ke9lB1I3f8XrKmYHM3c9AQJdgmyYvWAv6'),
       ('petr', 'petrov', 'petrovich', 'email3', 'ROLE_EMPLOYEE', false,
        '$2y$10$gh9C1nnMgokphEgYM/m2ke9lB1I3f8XrKmYHM3c9AQJdgmyYvWAv6');


INSERT INTO units(name)
VALUES ('кг'),
       ('шт');

INSERT INTO products(name, unit_id)
VALUES ('Яблоки', (SELECT id FROM units WHERE name = 'кг')),
       ('Молоко', (SELECT id FROM units WHERE name = 'шт'));


INSERT INTO manufactured_products(user_id, product_id, count, time)
VALUES ((SELECT id FROM users WHERE email = 'email1'), (SELECT id FROM products WHERE name = 'Яблоки'), 10,
        (SELECT current_date::timestamp + interval '10 hours')),
       ((SELECT id FROM users WHERE email = 'email1'), (SELECT id FROM products WHERE name = 'Яблоки'), 15,
        ((SELECT current_date::timestamp + interval '12 hours'))),
       ((SELECT id FROM users WHERE email = 'email2'), (SELECT id FROM products WHERE name = 'Яблоки'), 10,
        ((SELECT current_date::timestamp + interval '12 hours'))),
       ((SELECT id FROM users WHERE email = 'email2'), (SELECT id FROM products WHERE name = 'Молоко'), 11,
        ((SELECT current_date::timestamp + interval '13 hours')));

INSERT INTO working_norms(product_id, user_id, working_count, current_count, deadline, status)
VALUES ((SELECT id FROM products WHERE name = 'Яблоки'), (SELECT id FROM users WHERE email = 'email1'), 10, 11,
        (SELECT current_date::timestamp + interval '18 hours'), 'DONE'),
       ((SELECT id FROM products WHERE name = 'Яблоки'), (SELECT id FROM users WHERE email = 'email1'), 15, 5,
        (SELECT current_date::timestamp + interval '18 hours'), 'OVERDUE'),
       ((SELECT id FROM products WHERE name = 'Яблоки'), (SELECT id FROM users WHERE email = 'email2'), 10, 11,
        (SELECT current_date::timestamp + interval '18 hours'), 'DONE'),
       ((SELECT id FROM products WHERE name = 'Молоко'), (SELECT id FROM users WHERE email = 'email2'), 11, 20,
        (SELECT current_date::timestamp + interval '18 hours'), 'DONE');
