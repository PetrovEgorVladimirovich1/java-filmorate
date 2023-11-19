package ru.yandex.practicum.filmorate.storage.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class GenreDbStorageTest {
    private final JdbcTemplate jdbcTemplate;

    private GenreDbStorage genreDbStorage;

    @BeforeEach
    void setUp() {
        genreDbStorage = new GenreDbStorage(jdbcTemplate);
    }

    @Test
    void getGenres() {
        assertEquals(6, genreDbStorage.getGenres().size());
        assertEquals("Комедия", genreDbStorage.getGenres().get(0).getName());
    }

    @Test
    void getByIdGenre() {
        Genre genre = new Genre(6, "Боевик");
        assertEquals("Триллер", genreDbStorage.getByIdGenre(4).getName());
        assertEquals(genre, genreDbStorage.getByIdGenre(6));
    }
}