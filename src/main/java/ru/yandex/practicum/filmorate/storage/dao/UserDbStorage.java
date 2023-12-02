package ru.yandex.practicum.filmorate.storage.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.IncorrectParamException;
import ru.yandex.practicum.filmorate.model.Feed;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.dal.UserStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

@Component
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public void create(User user) {
        String sql = "INSERT INTO users (email, login, name, birthday) " +
                "VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql, new String[]{"id"});
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getName());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));
            return stmt;
        }, keyHolder);
        user.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
    }

    @Override
    public void update(User user) {
        String sql = "UPDATE users " +
                "SET email = ?, login = ?, name = ?, birthday = ? " +
                "WHERE id = ?";
        int count = jdbcTemplate.update(sql,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId());
        if (count == 0) {
            throw new IncorrectParamException("Неверный id!");
        }
    }

    @Override
    public List<User> getUsers() {
        String sql = "SELECT * FROM users";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs));
    }

    @Override
    public User getByIdUser(long id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        List<User> users = jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs), id);
        if (users.isEmpty()) {
            throw new IncorrectParamException("Неверный id!");
        }
        return users.get(0);
    }

    @Override
    public void addFriend(long idUser, long idFriend) {
        if (idUser <= 0 || idFriend <= 0) {
            throw new IncorrectParamException("Неверный id!");
        }
        String sql = "INSERT INTO friends (user_id, friend_id) " +
                "VALUES (?, ?)";
        String sqlFeed = "INSERT INTO feeds (user_id, entity_id, event_type, operation, times) " +
                "VALUES (?, ?, 'FRIEND', 'ADD', ?)";
        int count = jdbcTemplate.update(sql, idUser, idFriend);
        if (count == 0) {
            throw new IncorrectParamException("Неверный id!");
        }
        jdbcTemplate.update(sqlFeed, idUser, idFriend, Instant.now());
    }

    @Override
    public void deleteFriend(long idUser, long idFriend) {
        String sql = "DELETE FROM friends " +
                "WHERE user_id = ? AND friend_id = ?";
        String sqlFeed = "INSERT INTO feeds (user_id, entity_id, event_type, operation, times) " +
                "VALUES (?, ?, 'FRIEND', 'REMOVE', ?)";
        int count = jdbcTemplate.update(sql, idUser, idFriend);
        if (count == 0) {
            throw new IncorrectParamException("Неверный id!");
        }
        jdbcTemplate.update(sqlFeed, idUser, idFriend, Instant.now());
    }

    /**
     * @throws IncorrectParamException если юзер с введенныи id отсутствует
     */
    @Override
    public List<User> getUserFriends(long id) {
        //проверяем, существует ли пользователь в нужным id
        getByIdUser(id);
        String sql = "SELECT * FROM users WHERE id IN (SELECT friend_id FROM friends WHERE user_id = ?)";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs), id);
    }

    @Override
    public List<User> getUserFriendsCommonWithOtherUser(long idUser, long idOtherUser) {
        List<User> friendsUser = getUserFriends(idUser);
        List<User> friendsOtherUser = getUserFriends(idOtherUser);
        friendsUser.retainAll(friendsOtherUser);
        return friendsUser;
    }

    @Override
    public List<Feed> getFeeds(long id) {
        getByIdUser(id);
        String sql = "SELECT * FROM feeds WHERE user_id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeFeed(rs), id);
    }

    /**
     * метод для удаления записи о фильме из таблицы users.
     * предполагается, что данные из связанных таблиц БД удалит каскадом
     * т.е. при создании новых таблиц связанных с таблицей films надо указывать -
     * "REFERENCES users (id) ON DELETE CASCADE"
     *
     * @param userId id экземпляра класса Film
     * @throws IncorrectParamException при отсутствии элемента с данным id
     */
    @Override
    public void deleteUser(Integer userId) {
        String sql = "DELETE FROM users " +
                "WHERE id = ?";
        int count = jdbcTemplate.update(sql, userId);
        if (count == 0) {
            throw new IncorrectParamException("Невереный id!");
        }
    }

    private User makeUser(ResultSet rs) throws SQLException {
        return new User(rs.getLong("id"),
                rs.getString("email"),
                rs.getString("login"),
                rs.getString("name"),
                rs.getDate("birthday").toLocalDate());
    }

    private Feed makeFeed(ResultSet rs) throws SQLException {
        return new Feed(rs.getLong("event_id"),
                rs.getLong("user_id"),
                rs.getLong("entity_id"),
                rs.getString("event_type"),
                rs.getString("operation"),
                rs.getTimestamp("times").getTime());
    }
}
