package ru.yandex.practicum.filmorate.storage.dal;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    void create(Film film);

    void update(Film film);

    List<Film> getFilms();

    Film getByIdFilm(long id);

    void addLike(long idFilm, long idUser);

    void deleteLike(long idFilm, long idUser);

    List<Film> getPopularFilms(int count);
}
