CREATE ROLE pac_app LOGIN
  ENCRYPTED PASSWORD 'md554814fa3be05bb76c96d7399ca49d050'
  NOSUPERUSER INHERIT NOCREATEDB NOCREATEROLE NOREPLICATION;

CREATE DATABASE pac
  WITH OWNER = pac_app
       ENCODING = 'UTF8'
       TABLESPACE = pg_default
       LC_COLLATE = 'en_US.UTF-8'
       LC_CTYPE = 'en_US.UTF-8'
       CONNECTION LIMIT = -1;
GRANT CONNECT, TEMPORARY ON DATABASE pac TO public;
GRANT ALL ON DATABASE pac TO pac_app;

CREATE TABLE public.authority
(
  name character varying(50) NOT NULL,
  CONSTRAINT authority_pkey PRIMARY KEY (name)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.authority
  OWNER TO pac_app;

CREATE TABLE public.users
(
  id bigint NOT NULL,
  firstname character varying(50),
  lastname character varying(50),
  password_hash character varying(60) NOT NULL,
  username character varying(50) NOT NULL,
  CONSTRAINT users_pkey PRIMARY KEY (id),
  CONSTRAINT uk_r43af9ap4edm43mmtq01oddj6 UNIQUE (username)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.users
  OWNER TO pac_app;

CREATE TABLE public.user_authority
(
  user_id bigint NOT NULL,
  authority_name character varying(50) NOT NULL,
  CONSTRAINT user_authority_pkey PRIMARY KEY (user_id, authority_name),
  CONSTRAINT fk_5losscgu02yaej7prap7o6g5s FOREIGN KEY (user_id)
      REFERENCES public.users (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_tnnyxjpcvg2aj0d0i6ufnabm2 FOREIGN KEY (authority_name)
      REFERENCES public.authority (name) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.user_authority
  OWNER TO pac_app;

CREATE TABLE public.vote
(
  id bigint NOT NULL,
  created date NOT NULL,
  topic character varying(255) NOT NULL,
  creator_id bigint,
  CONSTRAINT vote_pkey PRIMARY KEY (id),
  CONSTRAINT fk_qpyah59400j8h4lndbpndbuv0 FOREIGN KEY (creator_id)
      REFERENCES public.users (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.vote
  OWNER TO pac_app;

CREATE TABLE public.vote_options
(
  id bigint NOT NULL,
  text character varying(255) NOT NULL,
  vote_id bigint,
  CONSTRAINT vote_options_pkey PRIMARY KEY (id),
  CONSTRAINT fk_2tlwa2e46up8fagtsnms3h17i FOREIGN KEY (vote_id)
      REFERENCES public.vote (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.vote_options
  OWNER TO pac_app;

CREATE TABLE public.user_votings
(
  id bigint NOT NULL,
  user_id bigint NOT NULL,
  vote_id bigint NOT NULL,
  vote_options_id bigint NOT NULL,
  CONSTRAINT user_votings_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.user_votings
  OWNER TO pac_app;

INSERT INTO public.authority (name) VALUES ('ROLE_ADMIN'), ('ROLE_USER');

INSERT INTO public.users (id, firstname, lastname, username, password_hash)
VALUE (1, '', 'admin', 'admin', '$2a$10$RmOY/u0rLquFG7d9aSnrXeTRCpFHCiFMt.Pfzq6C5iYcWwwgOuB7q'), 
      (2, 'user', 'test', 'test', '$2a$10$cVWjFzGqKilsH019AzgB9eP1FzZQ3gGrs7jfqgZD/3X8Z30uTu.KO');

INSERT INTO public.user_authority (user_id, authority_name)
VALUES (1, 'ROLE_ADMIN'), (1, 'ROLE_USER'), (2, 'ROLE_USER');