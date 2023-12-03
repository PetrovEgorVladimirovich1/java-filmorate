package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import ru.yandex.practicum.filmorate.exception.IncorrectParamException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.dal.FilmStorage;
import ru.yandex.practicum.filmorate.validate.Validate;

import java.util.List;

@Slf4j
@Service
//аннотация для создания конструктора
@RequiredArgsConstructor
public class FilmService {
    @Qualifier("filmDbStorage")
    private final FilmStorage filmStorage;
    private final UserService userService;

    public Film create(Film film, BindingResult bindingResult) {
        Validate.validate(bindingResult);
        filmStorage.create(film);
        log.info("Фильм успешно добавлен. {}", film);
        return film;
    }

    public Film update(Film film, BindingResult bindingResult) {
        Validate.validate(bindingResult);
        filmStorage.update(film);
        log.info("Фильм успешно обновлён. {}", film);
        return film;
    }

    public List<Film> getFilms() {
        return filmStorage.getFilms();
    }

    public Film getByIdFilm(long id) {
        return filmStorage.getByIdFilm(id);
    }

    public void addLike(long idFilm, long idUser) {
        filmStorage.addLike(idFilm, idUser);
        log.info("Добавлен лайк.");
    }

    public void deleteLike(long idFilm, long idUser) {
        filmStorage.deleteLike(idFilm, idUser);
        log.info("Лайк удалён.");
    }

    public List<Film> getPopularFilms(int count) {
        return filmStorage.getPopularFilms(count);
    }

    /**
     * Метод возвращает список фильмов которые не лайкнул userId, но лайкнули юзеры с походим набором лайков
     *
     * @param userId
     * @return список объектов класса Film
     * @throws IncorrectParamException если юзера с userId не существует
     */
    public List<Film> recommendations(Integer userId) {
        //проверяем существует ли user c таким id
        userService.getByIdUser(userId);
        return filmStorage.getUserRecommendations(userId);
    }

    /**
     * метод для удаления записи о фильме из таблицы films.
     * предполагается, что данные из связанных таблиц БД удалит каскадом
     * т.е. при создании новых таблиц связанных с таблицей films надо указывать -
     * "REFERENCES films (id) ON DELETE CASCADE"
     *
     * @param filmId id экземпляра класса Film
     * @throws IncorrectParamException при отсутствии элемента с данным id
     */
    public void deleteFilm(Integer filmId) {
        filmStorage.deleteFilm(filmId);
    }

    /**
     * метод определяет фильмы которые лайкнули оба юзера и сортирует из в порядке популярности
     *
     * @param userId   id  которому ищутся общие фильмы
     * @param friendId id юзера которого проверяют на наличие общих фильмов
     * @return список POJO класса Film
     * @throws IncorrectParamException если юзера с введенным id не существует
     */
    public List<Film> getCommonFilm(Integer userId, Integer friendId) {
        return filmStorage.getCommonFilms(userId, friendId);
    }
}
