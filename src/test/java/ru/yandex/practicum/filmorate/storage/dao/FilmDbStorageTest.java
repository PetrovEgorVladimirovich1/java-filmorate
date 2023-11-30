package ru.yandex.practicum.filmorate.storage.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.time.Month;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest
@AutoConfigureTestDatabase
class FilmDbStorageTest {
    @Autowired
    private FilmDbStorage filmDbStorage;
    @Autowired
    private UserDbStorage userDbStorage;

    private Film film;

    private Film filmTest;

    @BeforeEach
    void setUp() {
        film = new Film(1L, "Крепкий орешек.", "Описание.",
                LocalDate.of(1988, Month.JULY, 12), 133, new Mpa(4, "R"));
        filmDbStorage.create(film);
    }

    @Test
    void create() {
        filmTest = filmDbStorage.getByIdFilm(1);
        assertThat(filmTest)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(film);
    }

    @Test
    void update() {
        filmTest = new Film(1L, "Крепкий орешек 2.", "Описание.",
                LocalDate.of(1990, Month.JULY, 2), 124, new Mpa(4, "R"));
        filmDbStorage.update(filmTest);
        assertThat(filmDbStorage.getByIdFilm(1))
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(filmTest);
    }

    @Test
    void getFilms() {
        assertEquals(1, filmDbStorage.getFilms().size());
    }

    @Test
    void getByIdFilm() {
        assertThat(filmDbStorage.getByIdFilm(1))
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(film);
    }

    @Test
    void addLike() {
        User userTest = new User(2L, "egich-2011@mail.ru", "Egor", "Egor Petrov",
                LocalDate.of(2000, 9, 19));
        userDbStorage.create(userTest);
        filmDbStorage.addLike(film.getId(), userTest.getId());
        assertEquals(1, filmDbStorage.getPopularFilms(5, null, null).size());
    }

    @Test
    void deleteLike() {
        User userTest = new User(2L, "egich-2011@mail.ru", "Egor", "Egor Petrov",
                LocalDate.of(2000, 9, 19));
        User userTest1 = new User(3L, "egich-2011@mail.ru", "Egor", "Egor Petrov",
                LocalDate.of(2000, 9, 19));
        userDbStorage.create(userTest);
        userDbStorage.create(userTest1);
        filmDbStorage.addLike(film.getId(), userTest.getId());
        filmDbStorage.addLike(film.getId(), userTest1.getId());
        filmDbStorage.deleteLike(film.getId(), userTest.getId());
        assertEquals(1, filmDbStorage.getByIdFilm(film.getId()).getLikes().size());
    }

    @Test
    void getPopularFilms() {
        filmTest = new Film(2L, "Крепкий орешек 2.", "Описание.",
                LocalDate.of(1990, Month.JULY, 2), 124, new Mpa(4, "R"));
        filmDbStorage.create(filmTest);
        User userTest = new User(2L, "egich-2011@mail.ru", "Egor", "Egor Petrov",
                LocalDate.of(2000, 9, 19));
        User userTest1 = new User(3L, "egich-2011@mail.ru", "Egor", "Egor Petrov",
                LocalDate.of(2000, 9, 19));
        userDbStorage.create(userTest);
        userDbStorage.create(userTest1);
        filmDbStorage.addLike(film.getId(), userTest.getId());
        filmDbStorage.addLike(film.getId(), userTest1.getId());
        filmDbStorage.addLike(filmTest.getId(), userTest.getId());
        assertEquals(2, filmDbStorage.getPopularFilms(5, null, null).size());
    }
}