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

    /**
     * Метод возвращает список фильмов которые не лайкнул userId, но лайкнули юзеры с походим набором лайков
     * валидность параметра не проверяется
     *
     * @param userId
     * @return список объектов класса Film
     */
    List<Film> getUserRecommendations(Integer userId);
}
