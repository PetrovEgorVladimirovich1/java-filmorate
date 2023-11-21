package ru.yandex.practicum.filmorate.storage.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Genre;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest
@AutoConfigureTestDatabase
class GenreDbStorageTest {

    private GenreDbStorage genreDbStorage;

    @BeforeEach
    void setUp(@Autowired GenreDbStorage genreDbStorage) {
        this.genreDbStorage = genreDbStorage;
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