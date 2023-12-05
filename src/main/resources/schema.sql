DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS mpa CASCADE;
DROP TABLE IF EXISTS films CASCADE;
DROP TABLE IF EXISTS likes CASCADE;
DROP TABLE IF EXISTS genres CASCADE;
DROP TABLE IF EXISTS film_genres CASCADE;
DROP TABLE IF EXISTS friends CASCADE;
DROP TABLE IF EXISTS feeds CASCADE;
DROP TABLE IF EXISTS directors CASCADE;
DROP TABLE IF EXISTS films_director CASCADE;
DROP TABLE IF EXISTS reviews CASCADE;
DROP TABLE IF EXISTS useful CASCADE;

CREATE TABLE IF NOT EXISTS users (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    email VARCHAR(50) NOT NULL,
    login VARCHAR(50) NOT NULL,
    name VARCHAR(50),
    birthday DATE);

CREATE TABLE IF NOT EXISTS mpa (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR(50));

CREATE TABLE IF NOT EXISTS films (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    description VARCHAR(200),
    release_date DATE NOT NULL,
    duration INTEGER,
    mpa_id BIGINT REFERENCES mpa (id )ON DELETE CASCADE);

CREATE TABLE IF NOT EXISTS likes (
    film_id BIGINT NOT NULL REFERENCES films (id) ON DELETE CASCADE,
    user_id BIGINT NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    PRIMARY KEY (film_id, user_id));

CREATE TABLE IF NOT EXISTS genres (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR(50));

CREATE TABLE IF NOT EXISTS film_genres (
    film_id BIGINT REFERENCES films (id) ON DELETE CASCADE,
    genre_id BIGINT REFERENCES genres (id) ON DELETE CASCADE,
    PRIMARY KEY (film_id, genre_id));

CREATE TABLE IF NOT EXISTS friends (
    user_id BIGINT NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    friend_id BIGINT NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, friend_id));

CREATE TABLE IF NOT EXISTS Reviews(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    content VARCHAR(255),
    isPositive INTEGER,
    userId BIGINT REFERENCES users (id) ON DELETE CASCADE,
    filmId BIGINT REFERENCES films (id) ON DELETE CASCADE,
    useFul INTEGER
);

CREATE TABLE IF NOT EXISTS useful(
    reviewId BIGINT REFERENCES Reviews (id) ON DELETE CASCADE,
    userId BIGINT UNIQUE REFERENCES users (id) ON DELETE CASCADE,
    useFul INTEGER
);

CREATE TABLE IF NOT EXISTS directors (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR(50));

CREATE TABLE IF NOT EXISTS films_director (
    director_id BIGINT NOT NULL REFERENCES directors (id) ON DELETE CASCADE,
    film_id BIGINT NOT NULL REFERENCES films (id) ON DELETE CASCADE,
    PRIMARY KEY (director_id, film_id));


CREATE TABLE IF NOT EXISTS feeds (
   event_id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
   user_id BIGINT NOT NULL REFERENCES users (id),
   entity_id BIGINT NOT NULL,
   event_type VARCHAR(50) NOT NULL,
   operation VARCHAR(50) NOT NULL,
   times TIMESTAMP NOT NULL);
