-- delete all tables

drop table IF EXISTS endpoint_hit CASCADE;


-- create user related tables

create TABLE IF NOT EXISTS endpoint_hit (
  id       BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  app      VARCHAR(50) NOT NULL,
  uri      VARCHAR(200) NOT NULL,
  ip       VARCHAR(20) NOT NULL,
  hit_time TIMESTAMP NOT NULL,
  UNIQUE(id)
);
