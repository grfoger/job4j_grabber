create table if not exists schema (
    id serial primary key,
    name text,
    text text,
    link text unique,
    created timestamp
);