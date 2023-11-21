package ru.yandex.practicum.filmorate.storage.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.IncorrectParamException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.dal.UserStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
        int count = jdbcTemplate.update(sql, idUser, idFriend);
        if (count == 0) {
            throw new IncorrectParamException("Неверный id!");
        }
    }

    @Override
    public void deleteFriend(long idUser, long idFriend) {
        String sql = "DELETE FROM friends " +
                "WHERE user_id = ? AND friend_id = ?";
        int count = jdbcTemplate.update(sql, idUser, idFriend);
        if (count == 0) {
            throw new IncorrectParamException("Неверный id!");
        }
    }

    @Override
    public List<User> getUserFriends(long id) {
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

    private User makeUser(ResultSet rs) throws SQLException {
        return new User(rs.getLong("id"),
                rs.getString("email"),
                rs.getString("login"),
                rs.getString("name"),
                rs.getDate("birthday").toLocalDate());
    }
}
