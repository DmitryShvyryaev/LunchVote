DELETE FROM VOTES;
DELETE FROM USER_ROLES;
DELETE FROM USERS;
DELETE FROM DISHES;
DELETE FROM RESTAURANTS;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('admin', 'admin@email.com', 'xxx'),
       ('user1', 'user1@email.com', 'xxx'),
       ('user2', 'user2@email.com', 'xxx'),
       ('user3', 'user3@email.com', 'xxx'),
       ('user4', 'user4@email.com', 'xxx');

INSERT INTO user_roles (role, user_id)
VALUES ('USER', 100000),
       ('ADMIN', 100000),
       ('USER', 100001),
       ('USER', 100002),
       ('USER', 100003),
       ('USER', 100004);

INSERT INTO RESTAURANTS (NAME, DESCRIPTION)
VALUES ('Тануки', 'Ресторан японской кухни'),
       ('Макдональдс', 'Бургеры, наггетсы, кола'),
       ('Папа Джонс', 'Пицца так себе на дом');