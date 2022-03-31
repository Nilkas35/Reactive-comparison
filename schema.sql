 drop table IF EXISTS employees;
create table if not exists employees
(
    id   bigint not null
        primary key,
    name varchar(255)
);