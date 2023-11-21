package ru.yandex.practicum.filmorate.storage.dal;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    void create(User user);

    void update(User user);

    List<User> getUsers();

    User getByIdUser(long id);

    void addFriend(long idUser, long idFriend);

    void deleteFriend(long idUser, long idFriend);

    List<User> getUserFriends(long id);

    List<User> getUserFriendsCommonWithOtherUser(long idUser, long idOtherUser);
}
