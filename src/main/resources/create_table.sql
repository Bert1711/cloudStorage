-- create table files
-- (
--     id          bigserial not null PRIMARY KEY,
--     file_name   varchar(255),
--     size        integer   not null,
--     its_removed boolean   not null,
--     user_login  varchar(255)
-- );
--
-- create table users
-- (
--     id       bigserial not null,
--     username    varchar(255) unique,
--     password varchar(255),
--     primary key (id)
-- );

insert into users(username, password)
VALUES ( 'user1@mail.ru', 'password')