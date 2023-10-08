package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.FailIdException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();

    private int id = 0;

    private int addId() {
        return ++id;
    }

    private void validate(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            logInfo("Электронная почта не может быть пустой и должна содержать символ @!");
        }
        if (user.getLogin() == null || user.getLogin().isBlank()) {
            logInfo("Логин не может быть пустым и содержать пробелы!");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            logInfo("Дата рождения не может быть в будущем!");
        }
    }

    private void logInfo(String message) {
        ValidationException exception = new ValidationException(message);
        log.info("Валидация не пройдена! {}", exception.getMessage());
        throw exception;
    }

    @PostMapping
    public User create(@RequestBody User user) {
        validate(user);
        user.setId(addId());
        users.put(user.getId(), user);
        log.info("Пользователь успешно создан. {}", user);
        return user;
    }

    @PutMapping
    public User update(@RequestBody User user) {
        validate(user);
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            log.info("Пользователь успешно обновлён. {}", user);
        } else {
            log.info("Неверный id!");
            throw new FailIdException();
        }
        return user;
    }

    @GetMapping
    public List<User> getFilms() {
        return new ArrayList<>(users.values());
    }
}
