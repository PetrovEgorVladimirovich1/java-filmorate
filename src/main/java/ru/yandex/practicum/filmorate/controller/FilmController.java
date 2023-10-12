package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.FailIdException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validate.Validate;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();
    private int id = 0;

    private int addId() {
        return ++id;
    }

    private void logInfo(String message) {
        ValidationException exception = new ValidationException(message);
        log.info("Валидация не пройдена! {}", exception.getMessage());
        throw exception;
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film, BindingResult bindingResult) {
        try {
            Validate.validate(bindingResult);
            film.setId(addId());
            films.put(film.getId(), film);
            log.info("Фильм успешно добавлен. {}", film);
        } catch (ValidationException e) {
            logInfo(e.getMessage());
        }
        return film;
    }

    @PutMapping
    public Film update(@RequestBody @Valid Film film, BindingResult bindingResult) {
        try {
            Validate.validate(bindingResult);
            if (films.containsKey(film.getId())) {
                films.put(film.getId(), film);
                log.info("Фильм успешно обновлён. {}", film);
            } else {
                log.info("Неверный id!");
                throw new FailIdException();
            }
        } catch (ValidationException e) {
            logInfo(e.getMessage());
        }
        return film;
    }

    @GetMapping
    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }
}
