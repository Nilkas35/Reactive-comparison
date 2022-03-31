drop table if exists carts;
create table if not exists carts
(
    id   serial
        primary key,
    name text not null
);
drop table if exists members;
create table members
(
    id   serial
        primary key,
    name text not null
);