package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.FailIdException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
    UserController userController;
    User user;

    @BeforeEach
    void setUp() {
        userController = new UserController();
        user = new User();
        user.setName("Егор");
        user.setLogin("Egich");
        user.setEmail("egich-2011@mail.ru");
        user.setBirthday(LocalDate.of(1997, Month.OCTOBER, 7));
    }

    @Test
    void create() {
        assertEquals(user, userController.create(user),
                "Метод добавления должен возращать того же пользователя!");
        user.setName("    ");
        assertEquals(user.getLogin(), userController.create(user).getName(),
                "Метод валидации должен заменить пустое имя на логин!");
        user.setLogin("   ");
        assertThrows(ValidationException.class, () -> userController.create(user),
                "Метод валидации должен выбросить исключение!");
        user.setLogin("Egich");
        user.setEmail("   ");
        assertThrows(ValidationException.class, () -> userController.create(user),
                "Метод валидации должен выбросить исключение!");
        user.setEmail("egich-2011@mail.ru");
        user.setBirthday(LocalDate.now().plusDays(2));
        assertThrows(ValidationException.class, () -> userController.create(user),
                "Метод валидации должен выбросить исключение!");
    }

    @Test
    void update() {
        userController.create(user);
        user.setId(9999);
        assertThrows(FailIdException.class, () -> userController.update(user),
                "Должно выброситься исключение!");
        user.setId(1);
        user.setName("Егор 1");
        user.setLogin("Egich 1");
        user.setEmail("egich 1-2011@mail.ru");
        user.setBirthday(LocalDate.of(2000, Month.DECEMBER, 17));
        assertEquals(user, userController.update(user),
                "Метод обновления должен возращать того же пользователя!");
    }
}