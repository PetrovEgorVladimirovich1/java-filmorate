package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.FailIdException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();
    private final LocalDate birthdayMovie = LocalDate.of(1895, Month.DECEMBER, 28);
    private int id = 0;

    private int addId() {
        return ++id;
    }

    private void validate(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            logInfo("Название не может быть пустым!");
        }
        if (film.getDescription().length() >= 200) {
            logInfo("Максимальная длина описания — 200 символов!");
        }
        if (film.getReleaseDate().isBefore(birthdayMovie)) {
            logInfo("Дата релиза — не раньше 28 декабря 1895 года!");
        }
        if (film.getDuration() <= 0) {
            logInfo("Продолжительность фильма должна быть положительной!");
        }
    }

    private void logInfo(String message) {
        ValidationException exception = new ValidationException(message);
        log.info("Валидация не пройдена! {}", exception.getMessage());
        throw exception;
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
        validate(film);
        film.setId(addId());
        films.put(film.getId(), film);
        log.info("Фильм успешно добавлен. {}", film);
        return film;
    }

    @PutMapping
    public Film update(@RequestBody Film film) {
        validate(film);
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            log.info("Фильм успешно обновлён. {}", film);
        } else {
            log.info("Неверный id!");
            throw new FailIdException();
        }
        return film;
    }

    @GetMapping
    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }
}
