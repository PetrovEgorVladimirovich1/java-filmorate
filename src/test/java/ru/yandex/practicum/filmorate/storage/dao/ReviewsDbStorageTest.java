package ru.yandex.practicum.filmorate.storage.dao;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.time.Month;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest
@AutoConfigureTestDatabase
public class ReviewsDbStorageTest {
    @Autowired
    private FilmDbStorage filmDbStorage;
    @Autowired
    private UserDbStorage userDbStorage;

    private Film film;
    private User user1;
    private User user2;
    private User user3;

    @BeforeEach
    void setUp() {
        film = new Film(1L, "Крепкий орешек.", "Описание.",
                LocalDate.of(1988, Month.JULY, 12), 133, new Mpa(4, "R"));
        filmDbStorage.create(film);
        user1 = new User(1L, "csddsc@mail.ru", "aaaaa", "aaaa", LocalDate.now());
        user2 = new User(2L, "csddsc@mail.ru", "bbbb", "bbbb", LocalDate.now());
        user3 = new User(3L, "csddsc@mail.ru", "ccccc", "ccccc", LocalDate.now());
        userDbStorage.create(user1);
        userDbStorage.create(user2);
        userDbStorage.create(user3);
    }


}
