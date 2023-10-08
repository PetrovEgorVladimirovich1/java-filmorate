package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.FailIdException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {
    FilmController filmController;
    Film film;

    @BeforeEach
    void setUp() {
        filmController = new FilmController();
        film = new Film();
        film.setName("Крепкий орешек.");
        film.setDescription("Боевик.");
        film.setReleaseDate(LocalDate.of(1988, Month.JULY, 12));
        film.setDuration(133);
    }

    @Test
    void create() {
        assertEquals(film, filmController.create(film), "Метод добавления должен возращать тот же фильм!");
        film.setName("    ");
        assertThrows(ValidationException.class, () -> filmController.create(film),
                "Метод валидации должен выбросить исключение!");
        film.setName("Крепкий орешек.");
        film.setDescription("Пятеро друзей ( комик-группа «Шарло»), приезжают в город Бризуль. " +
                "Здесь они хотят разыскать господина Огюста Куглова, " +
                "который задолжал им деньги, а именно 20 миллионов. " +
                "о Куглов, который за время «своего отсутствия», стал кандидатом Коломбани." +
                "тут больше 200 символов. :)");
        assertThrows(ValidationException.class, () -> filmController.create(film),
                "Метод валидации должен выбросить исключение!");
        film.setDescription("Боевик.");
        film.setReleaseDate(LocalDate.of(1895, Month.DECEMBER, 27));
        assertThrows(ValidationException.class, () -> filmController.create(film),
                "Метод валидации должен выбросить исключение!");
        film.setReleaseDate(LocalDate.of(1988, Month.JULY, 12));
        film.setDuration(0);
        assertThrows(ValidationException.class, () -> filmController.create(film),
                "Метод валидации должен выбросить исключение!");
    }

    @Test
    void update() {
        filmController.create(film);
        film.setId(9999);
        assertThrows(FailIdException.class, () -> filmController.update(film),
                "Должно выброситься исключение!");
        film.setId(1);
        film.setName("Крепкий орешек 2.");
        film.setDescription("Боевик, триллер.");
        film.setReleaseDate(LocalDate.of(1990, Month.JULY, 2));
        film.setDuration(124);
        assertEquals(film, filmController.update(film), "Метод обновления должен возращать тот же фильм!");
    }
}