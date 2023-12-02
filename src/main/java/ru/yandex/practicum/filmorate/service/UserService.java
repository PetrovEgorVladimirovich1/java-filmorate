package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import ru.yandex.practicum.filmorate.model.Feed;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.dal.UserStorage;
import ru.yandex.practicum.filmorate.validate.Validate;

import java.util.List;

@Service
@Slf4j
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(@Qualifier("userDbStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    private void validate(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }

    public User create(User user, BindingResult bindingResult) {
        Validate.validate(bindingResult);
        validate(user);
        userStorage.create(user);
        log.info("Пользователь успешно создан. {}", user);
        return user;
    }

    public User update(User user, BindingResult bindingResult) {
        Validate.validate(bindingResult);
        validate(user);
        userStorage.update(user);
        log.info("Пользователь успешно обновлён. {}", user);
        return user;
    }

    public List<User> getUsers() {
        return userStorage.getUsers();
    }

    public User getByIdUser(long id) {
        return userStorage.getByIdUser(id);
    }

    public void addFriend(long idUser, long idFriend) {
        userStorage.addFriend(idUser, idFriend);
    }

    public void deleteFriend(long idUser, long idFriend) {
        userStorage.deleteFriend(idUser, idFriend);
    }

    public List<User> getUserFriends(long id) {
        return userStorage.getUserFriends(id);
    }

    public List<User> getUserFriendsCommonWithOtherUser(long idUser, long idOtherUser) {
        return userStorage.getUserFriendsCommonWithOtherUser(idUser, idOtherUser);
    }

    public List<Feed> getFeeds(long id) {
        return userStorage.getFeeds(id);
    }
}
