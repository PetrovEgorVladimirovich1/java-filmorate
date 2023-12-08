package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.exception.IncorrectParamException;
import ru.yandex.practicum.filmorate.mapper.FilmListMapper;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;
    private final FilmMapper filmMapper;
    private final FilmListMapper filmListMapper;

    @PostMapping
    public FilmDto createDto(@Valid @RequestBody FilmDto filmDto, BindingResult bindingResult) {

        Film filmDto1 = filmService.create(filmMapper.mapToModel(filmDto), bindingResult);
        return filmMapper.mapToDto(filmDto1);

    }

    @PutMapping
    public FilmDto updateDto(@Valid @RequestBody FilmDto filmDto, BindingResult bindingResult) {

        Film filmDto1 = filmService.update(filmMapper.mapToModel(filmDto), bindingResult);
        return filmMapper.mapToDto(filmDto1);
    }

    @GetMapping
    public List<FilmDto> getFilmDtos() {
        return filmListMapper.toDTOList(filmService.getFilms());
    }

    @GetMapping("/{id}")
    public FilmDto getByIdFilmDto(@PathVariable long id) {
        return filmMapper.mapToDto(filmService.getByIdFilm(id));
    }

    @GetMapping("/director/{directorId}")
    public List<FilmDto> getDirectorByLikesOrYearDto(@PathVariable("directorId") long id,
                                                     @RequestParam("sortBy") String name) {
        if (name.contains("year")) {
            return filmListMapper.toDTOList(filmService.getDirectorByYear(id));
        }
        if (name.contains("likes")) {
            return filmListMapper.toDTOList(filmService.getDirectorByLikes(id));
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
    public List<FilmDto> getPopularFilmsByGenreDto(@RequestParam(defaultValue = "10", required = false) int count,
                                                   @RequestParam(required = false) Integer genreId,
                                                   @RequestParam(required = false) Integer year) {

        return filmListMapper.toDTOList(filmService.getPopularFilmsByGenre(count, genreId, year));
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
    public List<FilmDto> getFilmsBySearchDto(
            @RequestParam(name = "query", defaultValue = "", required = false) String query,
            @RequestParam(name = "by", defaultValue = "", required = false) String by) {
        return filmListMapper.toDTOList(filmService.getFilmsBySearch(query, by));
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
    public List<FilmDto> getCommonFilmsDto(@RequestParam int userId,
                                           @RequestParam int friendId) {
        return filmListMapper.toDTOList(filmService.getCommonFilm(userId, friendId));
    }
}