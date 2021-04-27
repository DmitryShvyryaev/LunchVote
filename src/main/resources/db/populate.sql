DELETE
FROM VOTES;
DELETE
FROM USER_ROLES;
DELETE
FROM USERS;
DELETE
FROM DISHES;
DELETE
FROM RESTAURANTS;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('admin', 'admin@email.com', 'xxx111'),
       ('user1', 'user1@email.com', 'xxx222'),
       ('user2', 'user2@email.com', 'xxx333'),
       ('user3', 'user3@email.com', 'xxx444'),
       ('user4', 'user4@email.com', 'xxx555');

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

INSERT INTO DISHES (DATE, NAME, PRICE, RESTAURANT_ID)
VALUES ('2021-3-15', 'Мисо-суп', 210.50, 100005),
       ('2021-3-15', 'Калифорния', 420.35, 100005),
       ('2021-3-16', 'Суп с угрем', 300.99, 100005),
       ('2021-3-16', 'Филадельфия', 390.78, 100005),
       ('2021-3-15', 'Картофель Фри', 50.66, 100006),
       ('2021-3-15', 'Биг Мак', 170.05, 100006),
       ('2021-3-16', 'Наггетсы', 102.80, 100006),
       ('2021-3-16', 'Биг Тейсти', 199.99, 100006);

INSERT INTO VOTES (DATE, USER_ID, RESTAURANT_ID)
VALUES ('2021-3-15', 100000, 100005),
       ('2021-3-15', 100001, 100005),
       ('2021-3-15', 100002, 100005),
       ('2021-3-15', 100003, 100006),
       ('2021-3-15', 100004, 100007),
       ('2021-3-16', 100000, 100006),
       ('2021-3-16', 100001, 100006),
       ('2021-3-16', 100002, 100007),
       ('2021-3-16', 100003, 100007),
       ('2021-3-16', 100004, 100007);

