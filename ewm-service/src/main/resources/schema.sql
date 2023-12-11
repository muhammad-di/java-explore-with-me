-- delete all tables
DROP TABLE IF EXISTS comments CASCADE;
DROP TABLE IF EXISTS compilations_events CASCADE;
DROP TABLE IF EXISTS compilations CASCADE;
DROP TABLE IF EXISTS requests CASCADE;
DROP TABLE IF EXISTS events CASCADE;
DROP TABLE IF EXISTS categories CASCADE;
DROP TABLE IF EXISTS users CASCADE;

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

CREATE TABLE IF NOT EXISTS events (
	id                 BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY,
	created_on		   TIMESTAMP,
	annotation         VARCHAR(2000) NOT NULL,
	categories_id      BIGINT NOT NULL,
	initiator_id       BIGINT NOT NULL,
	description        VARCHAR(7000) NOT NULL,
	event_date         TIMESTAMP NOT NULL,
	lat                DOUBLE PRECISION NOT NULL,
	lon                DOUBLE PRECISION NOT NULL,
	paid               BOOL NOT NULL DEFAULT false,
	participant_limit  SMALLINT NOT NULL DEFAULT 0,
	published_on       TIMESTAMP,
	request_moderation BOOL NOT NULL DEFAULT true,
	state              VARCHAR(9) NOT NULL DEFAULT 'PENDING',
	title              VARCHAR(120) NOT NULL,
	CONSTRAINT events_pkey PRIMARY KEY (id),
	CONSTRAINT fk_events_to_categories FOREIGN KEY (categories_id) REFERENCES categories(id),
	CONSTRAINT fk_events_to_users FOREIGN KEY (initiator_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS requests (
	id           BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY,
	created	     TIMESTAMP,
	event_id     BIGINT NOT NULL,
	requester_id BIGINT NOT NULL,
	status       VARCHAR(9) NOT NULL DEFAULT 'PENDING',
	CONSTRAINT requests_pkey PRIMARY KEY (id),
	CONSTRAINT fk_requests_to_events FOREIGN KEY (event_id) REFERENCES events(id),
	CONSTRAINT fk_requests_to_users FOREIGN KEY (requester_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS compilations (
	id     BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY,
    title  VARCHAR(50) NOT NULL,
	pinned BOOL NOT NULL DEFAULT false,
	CONSTRAINT compilations_pkey PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS compilations_events (
	id              BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY,
	compilation_id	BIGINT NOT NULL,
	event_id        BIGINT NOT NULL,
	CONSTRAINT compilations_events_pkey PRIMARY KEY (id),
	CONSTRAINT fk_compilations_events_to_compilations FOREIGN KEY (compilation_id) REFERENCES compilations(id)
);

CREATE TABLE IF NOT EXISTS comments (
	id           BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY,
	created	     TIMESTAMP NOT NULL,
	event_id     BIGINT NOT NULL,
	commenter_id BIGINT NOT NULL,
	text         VARCHAR(2000) NOT NULL,
	state        VARCHAR(9) NOT NULL DEFAULT 'PUBLISHED',
	CONSTRAINT comments_pkey PRIMARY KEY (id),
	CONSTRAINT fk_comments_to_events FOREIGN KEY (event_id) REFERENCES events(id),
	CONSTRAINT fk_comments_to_users FOREIGN KEY (commenter_id) REFERENCES users(id)
);

