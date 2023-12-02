package ru.yandex.practicum.filmorate.storage.dal;

import ru.yandex.practicum.filmorate.exception.IncorrectParamException;
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

    List<Film> getDirectorByLikes(long id);

    List<Film> getDirectorByYear(long id);

    /**
     * Метод возвращает список фильмов которые не лайкнул userId, но лайкнули юзеры с походим набором лайков
     * валидность параметра не проверяется
     *
     * @param userId
     * @return список объектов класса Film
     */
    List<Film> getUserRecommendations(Integer userId);

    /**
     * метод для удаления записи о фильме из таблицы films.
     * предполагается, что данные из связанных таблиц БД удалит каскадом
     * т.е. при создании новых таблиц связанных с таблицей films надо указывать -
     * "REFERENCES films (id) ON DELETE CASCADE"
     *
     * @param filmId id экземпляра класса Film
     * @throws IncorrectParamException при отсутствии элемента с данным id
     */
    void deleteFilm(Integer filmId);
}
