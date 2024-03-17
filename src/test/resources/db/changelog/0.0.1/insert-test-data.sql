--liquibase formatted sql

--changeset Andrey Antonov:1
--comment adding a farm owner
INSERT INTO users(first_name, middle_name, last_name, email, "role", enabled, "password")
VALUES ('Andrey', 'Antonov', 'Andreevich', 'andreyantonov2702@gmail.com', 'ROLE_OWNER', true,
        '$2y$10$xloR8aLC15XtGOMyKMHYHeUYTk/lxbcHyoQ.C50MNm.fn7bWdcxc6');
