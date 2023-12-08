package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.FeedDto;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.exception.IncorrectParamException;
import ru.yandex.practicum.filmorate.mapper.FeedListMapper;
import ru.yandex.practicum.filmorate.mapper.FilmListMapper;
import ru.yandex.practicum.filmorate.mapper.UserListMapper;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
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
    private final UserMapper userMapper;
    private final UserListMapper userListMapper;
    private final FilmListMapper filmListMapper;
    private final FeedListMapper feedListMapper;

    @PostMapping
    public UserDto createDto(@Valid @RequestBody UserDto userDto, BindingResult bindingResult) {
        return userMapper.mapToDto(userService.create(userMapper.mapToModel(userDto), bindingResult));
    }

    @PutMapping
    public UserDto updateDto(@Valid @RequestBody UserDto userDto, BindingResult bindingResult) {
        return userMapper.mapToDto(userService.update(userMapper.mapToModel(userDto), bindingResult));
    }

    @GetMapping
    public List<UserDto> getUserDtos() {
        return userListMapper.toDTOList(userService.getUsers());
    }

    @GetMapping("/{id}")
    public UserDto getByIdUserDto(@PathVariable long id) {
        return userMapper.mapToDto(userService.getByIdUser(id));
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
    public List<UserDto> getUserFriendsDto(@PathVariable long id) {
        return userListMapper.toDTOList(userService.getUserFriends(id));
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<UserDto> getUserFriendsCommonWithOtherUserDto(@PathVariable("id") long idUser, @PathVariable("otherId") long idOtherUser) {
        return userListMapper.toDTOList(userService.getUserFriendsCommonWithOtherUser(idUser, idOtherUser));
    }

    @GetMapping("/{id}/feed")
    public List<FeedDto> getFeedDtos(@PathVariable("id") long id) {
        return feedListMapper.toDTOList(userService.getFeeds(id));
    }

    /**
     * Метод возвращает список фильмов которые не лайкнул userId, но лайкнули юзеры с походим набором лайков
     *
     * @param userId
     * @return список объектов класса Film
     * @throws IncorrectParamException если юзера с userId не существует
     */
    @GetMapping("/{id}/recommendations")
    public List<FilmDto> getRecommendations(@PathVariable("id") Integer userId) {
        return filmListMapper.toDTOList(filmService.recommendations(userId));
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
