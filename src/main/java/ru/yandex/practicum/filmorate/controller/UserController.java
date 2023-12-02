package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.IncorrectParamException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Feed;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final FilmService filmService;

    @PostMapping
    public User create(@Valid @RequestBody User user, BindingResult bindingResult) {
        return userService.create(user, bindingResult);
    }

    @PutMapping
    public User update(@Valid @RequestBody User user, BindingResult bindingResult) {
        return userService.update(user, bindingResult);
    }

    @GetMapping
    public List<User> getUsers() {
        return userService.getUsers();
    }

    @GetMapping("/{id}")
    public User getByIdUser(@PathVariable long id) {
        return userService.getByIdUser(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable("id") long idUser, @PathVariable("friendId") long idFriend) {
        userService.addFriend(idUser, idFriend);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable("id") long idUser, @PathVariable("friendId") long idFriend) {
        userService.deleteFriend(idUser, idFriend);
    }

    @GetMapping("/{id}/friends")
    public List<User> getUserFriends(@PathVariable long id) {
        return userService.getUserFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getUserFriendsCommonWithOtherUser(@PathVariable("id") long idUser, @PathVariable("otherId") long idOtherUser) {
        return userService.getUserFriendsCommonWithOtherUser(idUser, idOtherUser);
    }

    @GetMapping("/{id}/feed")
    public List<Feed> getFeeds(@PathVariable("id") long id) {
        return userService.getFeeds(id);
    }

    /**
     * Метод возвращает список фильмов которые не лайкнул userId, но лайкнули юзеры с походим набором лайков
     *
     * @param userId
     * @return список объектов класса Film
     * @throws IncorrectParamException если юзера с userId не существует
     */
    @GetMapping("/{id}/recommendations")
    public List<Film> getRecommendations(@PathVariable("id") Integer userId) {
        return filmService.recommendations(userId);
    }

    /**
     * метод для удаления записи о юзере из таблицы users.
     * предполагается, что данные из связанных таблиц БД удалит каскадом
     * т.е. при создании новых таблиц связанных с таблицей users надо указывать -
     * "REFERENCES users (id) ON DELETE CASCADE"
     *
     * @param id id экземпляра класса User
     * @throws IncorrectParamException при отсутствии элемента с данным id
     */
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable("id") Integer id) {
        userService.deleteUser(id);
    }
}
