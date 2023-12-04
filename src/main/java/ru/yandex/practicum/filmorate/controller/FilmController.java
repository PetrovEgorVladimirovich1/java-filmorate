package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.IncorrectParamException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film, BindingResult bindingResult) {
        return filmService.create(film, bindingResult);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film, BindingResult bindingResult) {
        return filmService.update(film, bindingResult);
    }

    @GetMapping
    public List<Film> getFilms() {
        return filmService.getFilms();
    }

    @GetMapping("/{id}")
    public Film getByIdFilm(@PathVariable long id) {
        return filmService.getByIdFilm(id);
    }

    @GetMapping("/director/{directorId}")
    public List<Film> getDirectorByLikesOrYear(@PathVariable("directorId") long id,
                                               @RequestParam("sortBy") String name) {
        if (name.contains("year")) {
            return filmService.getDirectorByYear(id);
        }
        if (name.contains("likes")) {
            return filmService.getDirectorByLikes(id);
        }
        throw new IncorrectParamException("Неверный sortBy");
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable("id") long idFilm, @PathVariable("userId") long idUser) {
        filmService.addLike(idFilm, idUser);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable("id") long idFilm, @PathVariable("userId") long idUser) {
        filmService.deleteLike(idFilm, idUser);
    }

    /**
     * метод определяет фильмы с максимальным количеством лайков среди всех фильмов (
     * параметры genreId И year отсутствуют), среди фильмов одного года (параметр genreId
     * отсутствует), среди фильмов одного жанра (параметр year отсутствует),
     * либо среди фильмов определенного жанра и года выпуска.
     * при введении неверного года возвращается пустой список
     * при введении неверного id жанра возвращается пустой список
     *
     * @param count   максимальеая длина возвращаемого списка
     * @param genreId id жанра по которому ведется поиск
     * @param year    год выхода фильма в прокат
     * @return возвращает список объектов класса Film
     */
    @GetMapping("/popular")
    public List<Film> getPopularFilmsByGenre(@RequestParam(defaultValue = "10", required = false) int count,
                                             @RequestParam(required = false) Integer genreId,
                                             @RequestParam(required = false) Integer year) {
        return filmService.getPopularFilmsByGenre(count, genreId, year);
    }


    /**
     * метод для удаления записи о фильме из таблицы films.
     * предполагается, что данные из связанных таблиц БД удалит каскадом
     * т.е. при создании новых таблиц связанных с таблицей films надо указывать -
     * "REFERENCES films (id) ON DELETE CASCADE"
     *
     * @param id id экземпляра класса Film
     * @throws IncorrectParamException при отсутствии элемента с данным id
     */
    @DeleteMapping("/{id}")
    public void deleteFilm(@PathVariable("id") Integer id) {
        filmService.deleteFilm(id);
    }

    @GetMapping("/search")
    public List<Film> getFilmsBySearch(
            @RequestParam(name = "query", defaultValue = "", required = false) String query,
            @RequestParam(name = "by", defaultValue = "", required = false) String by) {
        return filmService.getFilmsBySearch(query, by);
    }

    /**
     * метод определяет фильмы которые лайкнули оба юзера и сортирует из в порядке популярности
     *
     * @param userId   id  которому ищутся общие фильмы
     * @param friendId id юзера которого проверяют на наличие общих фильмов
     * @return список POJO класса Film
     * @throws IncorrectParamException если юзера с введенным id не существует
     */
    @GetMapping("/common")
    public List<Film> getCommonFilms(@RequestParam int userId,
                                     @RequestParam int friendId) {
        return filmService.getCommonFilm(userId, friendId);
    }
}