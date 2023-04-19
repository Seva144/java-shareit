-- drop table if exists comments;
-- drop table if exists booking;
-- drop table if exists item;
-- drop table if exists users;

create table if not exists users
(
    id    BIGINT generated by default as identity primary key,
    name  CHARACTER VARYING      not null,
    email CHARACTER VARYING(512) not null unique
);


create table if not exists item
(
    id           BIGINT generated by default as identity primary key,
    name         CHARACTER VARYING(500) not null,
    description  CHARACTER VARYING(500),
    is_available BOOLEAN                not null,
    owner        BIGINT references users (id)
);


create table if not exists booking
(
    id         BIGINT generated by default as identity primary key,
    start_time timestamp without time zone,
    end_time   timestamp without time zone,
    item       BIGINT references item (id),
    booker     BIGINT references users (id),
    status     CHARACTER VARYING(500) not null
);

create table if not exists comments
(
    id          BIGINT generated by default as identity primary key,
    text        CHARACTER VARYING(500),
    item_id     BIGINT references item (id),
    user_id     BIGINT references users (id),
    create_time TIMESTAMP without time zone
);

--
-- delete
-- from comments;
-- alter table comments
--     alter column
--         id restart with 1;
--
-- delete
-- from booking;
-- alter table booking
--     alter column
--         id restart with 1;
--
-- delete
-- from item;
-- alter table item
--     alter column
--         id restart with 1;
--
-- delete
-- from users;
-- alter table users
--     alter column
--         id restart with 1;


-- -- drop type booking_status cascade;
-- -- create type booking_status as ENUM ('WAITING','APPROVED', 'REJECTED', 'CANCELED');




