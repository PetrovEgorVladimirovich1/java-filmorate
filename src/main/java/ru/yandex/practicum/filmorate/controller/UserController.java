package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.FailIdException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validate.Validate;

import javax.validation.Valid;
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
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }

    private void logInfo(String message) {
        ValidationException exception = new ValidationException(message);
        log.info("Валидация не пройдена! {}", exception.getMessage());
        throw exception;
    }

    @PostMapping
    public User create(@Valid @RequestBody User user, BindingResult bindingResult) {
        try {
            Validate.validate(bindingResult);
            validate(user);
            user.setId(addId());
            users.put(user.getId(), user);
            log.info("Пользователь успешно создан. {}", user);
        } catch (ValidationException e) {
            logInfo(e.getMessage());
        }
        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody User user, BindingResult bindingResult) {
        try {
            Validate.validate(bindingResult);
            validate(user);
            if (users.containsKey(user.getId())) {
                users.put(user.getId(), user);
                log.info("Пользователь успешно обновлён. {}", user);
            } else {
                log.info("Неверный id!");
                throw new FailIdException();
            }
        } catch (ValidationException e) {
            logInfo(e.getMessage());
        }
        return user;
    }

    @GetMapping
    public List<User> getFilms() {
        return new ArrayList<>(users.values());
    }
}
