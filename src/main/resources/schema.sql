drop table if exists comments;
drop table if exists booking;
drop table if exists item;
drop table if exists requests;
drop table if exists users;

create table if not exists users
(
    id    bigint generated by default as identity primary key,
    name  character varying      not null,
    email character varying(500) not null unique
);

create table if not exists requests
(
    id           bigint generated by default as identity primary key,
    description  character varying(500),
    requestor_id bigint references users (id),
    created      timestamp without time zone not null
);

create table if not exists item
(
    id           bigint generated by default as identity primary key,
    name         character varying(500) not null,
    description  character varying(500),
    is_available boolean                not null,
    owner        bigint references users (id),
    request_id   bigint
);


create table if not exists booking
(
    id         bigint generated by default as identity primary key,
    start_time timestamp without time zone,
    end_time   timestamp without time zone,
    item       bigint references item (id),
    booker     bigint references users (id),
    status     character varying(500) not null
);

create table if not exists comments
(
    id          bigint generated by default as identity primary key,
    text        character varying(500),
    item_id     bigint references item (id),
    user_id     bigint references users (id),
    create_time timestamp without time zone
);








