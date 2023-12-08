package ru.yandex.practicum.filmorate.storage.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Director;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest
@AutoConfigureTestDatabase
class DirectorDbStorageTest {

    @Autowired
    private DirectorDbStorage directorDbStorage;

    private Director director;
    private Director testDirector;

    @BeforeEach
    void setUp() {
        director = new Director(1L, "Стивен Спилберг");
        directorDbStorage.createDirector(director);
    }

    @Test
    void checkCreateDirectors() {
        testDirector = directorDbStorage.getDirectorById(1);
        assertThat(testDirector)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(director);
    }

    @Test
    void checkUpdateDirector() {
        testDirector = new Director(1L, "Роман Полански");
        directorDbStorage.updateDirector(testDirector);
        assertThat(directorDbStorage.getDirectorById(1))
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(testDirector);
    }

    @Test
    void checkDellDirectorById() {
        directorDbStorage.dellDirectorById(1);
        assertEquals(0, directorDbStorage.getDirectors().size());
    }

    @Test
    void checkGetDirectors() {
        assertEquals(1, directorDbStorage.getDirectors().size());
    }

    @Test
    void checkGetDirectorById() {
        assertThat(directorDbStorage.getDirectorById(1))
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(director);
    }
}
