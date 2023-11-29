DROP TABLE IF EXISTS users, mpa, films, likes, genres, film_genres, friends;

CREATE TABLE IF NOT EXISTS users (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    email varchar(50) NOT NULL,
    login varchar(50) NOT NULL,
    name varchar(50),
    birthday date);

CREATE TABLE IF NOT EXISTS mpa (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name varchar(50));

CREATE TABLE IF NOT EXISTS films (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name varchar(50) NOT NULL,
    description varchar(200),
    release_date date NOT NULL,
    duration integer,
    mpa_id BIGINT REFERENCES mpa (id));

CREATE TABLE IF NOT EXISTS likes (
    film_id BIGINT NOT NULL REFERENCES films (id),
    user_id BIGINT NOT NULL REFERENCES users (id),
    PRIMARY KEY (film_id, user_id));

CREATE TABLE IF NOT EXISTS genres (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name varchar(50));

CREATE TABLE IF NOT EXISTS film_genres (
    film_id BIGINT REFERENCES films (id),
    genre_id BIGINT REFERENCES genres (id),
    PRIMARY KEY (film_id, genre_id));

CREATE TABLE IF NOT EXISTS friends (
    user_id BIGINT NOT NULL REFERENCES users (id),
    friend_id BIGINT NOT NULL REFERENCES users (id),
    PRIMARY KEY (user_id, friend_id));

CREATE TABLE IF NOT EXISTS Reviews(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    content VARCHAR,
    isPositive INTEGER,
    userId BIGINT REFERENCES users (id),
    filmId BIGINT REFERENCES films (id),
    useFul INTEGER
);

CREATE TABLE IF NOT EXISTS useful(
     reviewId BIGINT REFERENCES Reviews (id),
     userId BIGINT REFERENCES users (id),
     useFul INTEGER
);
