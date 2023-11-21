package ru.yandex.practicum.filmorate.storage.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Mpa;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest
@AutoConfigureTestDatabase
class MpaDbStorageTest {
    @Autowired
    private MpaDbStorage mpaDbStorage;

    @Test
    void getMpa() {
        assertEquals(5, mpaDbStorage.getMpa().size());
        assertEquals("G", mpaDbStorage.getMpa().get(0).getName());
    }

    @Test
    void getByIdMpa() {
        Mpa mpa = new Mpa(5, "NC-17");
        assertEquals("R", mpaDbStorage.getByIdMpa(4).getName());
        assertEquals(mpa, mpaDbStorage.getByIdMpa(5));
    }
}