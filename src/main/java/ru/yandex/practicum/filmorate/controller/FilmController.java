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

    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10", required = false) int count) {
        return filmService.getPopularFilms(count);
    }
}
