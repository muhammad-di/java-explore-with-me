-- delete all tables
drop table IF EXISTS users CASCADE;
drop table IF EXISTS categories CASCADE;



-- create user related tables

create table IF NOT EXISTS users (
  id    BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  name 	VARCHAR(250) NOT NULL,
  email VARCHAR(254) UNIQUE NOT NULL,
  UNIQUE(id)
);

create table IF NOT EXISTS categories (
  id    BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  name 	VARCHAR(50) NOT NULL,
  UNIQUE(id)
);

