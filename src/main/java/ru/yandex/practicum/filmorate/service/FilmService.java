package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.dal.FilmStorage;
import ru.yandex.practicum.filmorate.validate.Validate;

import java.util.List;

@Slf4j
@Service
public class FilmService {
    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

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
}
