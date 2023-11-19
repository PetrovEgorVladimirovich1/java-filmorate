package ru.yandex.practicum.filmorate.storage.memory;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.IncorrectParamException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.dal.UserStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();

    private long id = 0;

    private long addId() {
        return ++id;
    }

    @Override
    public void create(User user) {
        user.setId(addId());
        users.put(user.getId(), user);
    }

    @Override
    public void update(User user) {
        if (!users.containsKey(user.getId())) {
            throw new IncorrectParamException("Неверный id!");
        }
        users.put(user.getId(), user);
    }

    @Override
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User getByIdUser(long id) {
        return users.getOrDefault(id, null);
    }

    @Override
    public void addFriend(long idUser, long idFriend) {

    }

    @Override
    public void deleteFriend(long idUser, long idFriend) {

    }

    @Override
    public List<User> getUserFriends(long id) {
        return null;
    }

    @Override
    public List<User> getUserFriendsCommonWithOtherUser(long idUser, long idOtherUser) {
        return null;
    }
}
