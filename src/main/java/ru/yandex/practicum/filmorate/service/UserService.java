package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import ru.yandex.practicum.filmorate.exception.FailIdException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.validate.Validate;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
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
        if (userStorage.getByIdUser(id) == null) {
            throw new FailIdException("Неверный id!");
        }
        return userStorage.getByIdUser(id);
    }

    public void addFriend(long idUser, long idFriend) {
        if (userStorage.getByIdUser(idUser) == null || userStorage.getByIdUser(idFriend) == null) {
            throw new FailIdException("Неверный id!");
        }
        userStorage.getByIdUser(idUser).addFriend(idFriend);
        userStorage.getByIdUser(idFriend).addFriend(idUser);
    }

    public void deleteFriend(long idUser, long idFriend) {
        if (userStorage.getByIdUser(idUser) == null || userStorage.getByIdUser(idFriend) == null) {
            throw new FailIdException("Неверный id!");
        }
        userStorage.getByIdUser(idUser).deleteFriend(idFriend);
        userStorage.getByIdUser(idFriend).deleteFriend(idUser);
    }

    public List<User> getUserFriends(long id) {
        if (userStorage.getByIdUser(id) == null) {
            throw new FailIdException("Неверный id!");
        }
        List<User> friends = new ArrayList<>();
        for (long idFriend : userStorage.getByIdUser(id).getFriends()) {
            friends.add(userStorage.getByIdUser(idFriend));
        }
        return friends;
    }

    public List<User> getUserFriendsCommonWithOtherUser(long idUser, long idOtherUser) {
        if (userStorage.getByIdUser(idUser) == null || userStorage.getByIdUser(idOtherUser) == null) {
            throw new FailIdException("Неверный id!");
        }
        List<User> friendsCommon = new ArrayList<>();
        for (long id : userStorage.getByIdUser(idUser).getFriends()) {
            if (userStorage.getByIdUser(idOtherUser).getFriends().contains(id)) {
                friendsCommon.add(userStorage.getByIdUser(id));
            }
        }
        return friendsCommon;
    }
}
