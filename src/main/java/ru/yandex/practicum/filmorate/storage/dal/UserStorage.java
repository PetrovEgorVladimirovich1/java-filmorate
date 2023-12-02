package ru.yandex.practicum.filmorate.storage.dal;

import ru.yandex.practicum.filmorate.model.Feed;
import ru.yandex.practicum.filmorate.exception.IncorrectParamException;
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

    List<Feed> getFeeds(long id);

    /**
     * метод для удаления записи о юзере из таблицы users.
     * предполагается, что данные из связанных таблиц БД удалит каскадом
     * т.е. при создании новых таблиц связанных с таблицей films надо указывать -
     * "REFERENCES users (id) ON DELETE CASCADE"
     *
     * @param userId id экземпляра класса Film
     * @throws IncorrectParamException при отсутствии элемента с данным id
     */
    void deleteUser(Integer userId);
}
