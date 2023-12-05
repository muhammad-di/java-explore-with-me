-- delete all tables
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS categories CASCADE;



-- create user related tables

CREATE TABLE IF NOT EXISTS users (
	id    BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY,
	name  varchar(250) NOT NULL,
	email varchar(254) NOT NULL,
	CONSTRAINT users_email_key UNIQUE (email),
	CONSTRAINT users_pkey PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS categories (
  id   BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY,
  name VARCHAR(50) NOT NULL,
  CONSTRAINT categories_name_key UNIQUE (name),
  CONSTRAINT categories_pkey PRIMARY KEY (id)
);



