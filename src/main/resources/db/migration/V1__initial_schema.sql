drop table if exists user_action cascade;

create table user_action
(
    user_action_id   bigserial    not null primary key,
    user_id          bigint       not null,
    user_name        varchar(255),
    datetime         timestamp without time zone default now()
);