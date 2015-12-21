DELETE FROM user_roles;
DELETE FROM users;
DELETE FROM user_meal;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', 'password');

INSERT INTO users (name, email, password)
VALUES ('Admin', 'admin@gmail.com', 'admin');

INSERT INTO user_roles (role, user_id) VALUES
  ('ROLE_USER', 100000),
  ('ROLE_ADMIN', 100001);

INSERT INTO user_meal (datetime, description, calories, user_id)
VALUES ('2015-12-21 23:59:59.610000', 'Завтрак', 500, 100000);

INSERT INTO user_meal (datetime, description, calories, user_id)
VALUES ('2015-12-21 13:24:09.610000', 'Обед', 600, 100000);

INSERT INTO user_meal (datetime, description, calories, user_id)
VALUES ('2015-12-21 21:24:09.610000', 'Ужин', 300, 100000);

INSERT INTO user_meal (datetime, description, calories, user_id)
VALUES ('2015-12-20 08:24:09.610000', 'Завтрак', 305, 100000);

INSERT INTO user_meal (datetime, description, calories, user_id)
VALUES ('2015-12-20 12:24:09.610000', 'Обед', 400, 100000);

INSERT INTO user_meal (datetime, description, calories, user_id)
VALUES ('2015-12-20 18:24:09.610000', 'Ужин', 500, 100000);

INSERT INTO user_meal (datetime, description, calories, user_id)
VALUES ('2015-12-21 01:24:09.610000', 'Завтрак', 500, 100001);

INSERT INTO user_meal (datetime, description, calories, user_id)
VALUES ('2015-12-21 13:24:09.610000', 'Обед', 600, 100001);

INSERT INTO user_meal (datetime, description, calories, user_id)
VALUES ('2015-12-21 21:24:09.610000', 'Ужин', 300, 100001);

INSERT INTO user_meal (datetime, description, calories, user_id)
VALUES ('2015-12-20 08:24:09.610000', 'Завтрак', 305, 100001);

INSERT INTO user_meal (datetime, description, calories, user_id)
VALUES ('2015-12-20 12:24:09.610000', 'Обед', 400, 100001);

INSERT INTO user_meal (datetime, description, calories, user_id)
VALUES ('2015-12-20 18:24:09.610000', 'Ужин', 500, 100001);