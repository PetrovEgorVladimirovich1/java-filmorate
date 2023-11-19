DROP TABLE IF EXISTS users, mpa, films, likes, genres, film_genres, friends;

CREATE TABLE IF NOT EXISTS users (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    email varchar NOT NULL,
    login varchar NOT NULL,
    name varchar,
    birthday date);

CREATE TABLE IF NOT EXISTS mpa (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name varchar);

CREATE TABLE IF NOT EXISTS films (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name varchar NOT NULL,
    description varchar(200),
    release_date date NOT NULL,
    duration integer,
    mpa_id integer REFERENCES mpa (id));

CREATE TABLE IF NOT EXISTS likes (
    film_id integer NOT NULL REFERENCES films (id),
    user_id integer NOT NULL REFERENCES users (id),
    PRIMARY KEY (film_id, user_id));

CREATE TABLE IF NOT EXISTS genres (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name varchar);

CREATE TABLE IF NOT EXISTS film_genres (
    film_id integer REFERENCES films (id),
    genre_id integer REFERENCES genres (id),
    PRIMARY KEY (film_id, genre_id));

CREATE TABLE IF NOT EXISTS friends (
    user_id integer NOT NULL REFERENCES users (id),
    friend_id integer NOT NULL REFERENCES users (id),
    PRIMARY KEY (user_id, friend_id));
