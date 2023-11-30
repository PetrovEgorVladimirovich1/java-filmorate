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

    /**
     * метод определяет фильмы с максимальным количеством лайков среди всех фильмов (genreId == null И
     * year == null), среди фильмов одного года (genreId == null), среди фильмов одного жанра (year == null),
     * либо среди фильмов определенного жанра и года выпуска.
     * при введении неверного года возвращается пустой список
     * при введении неверного id жанра возвращается пустой список
     *
     * @param count   максимальеая длина возвращаемого списка
     * @param genreId id жанра по которому ведется поиск
     * @param year    год выхода фильма в прокат
     * @return возвращает список объектов класса Film
     */
    List<Film> getPopularFilms(int count, Integer genreId, Integer year);
}
