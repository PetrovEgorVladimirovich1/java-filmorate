package ru.yandex.practicum.filmorate.storage.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.IncorrectParamException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.dal.DirectorStorage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

@Component
public class DirectorDbStorage implements DirectorStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DirectorDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Director createDirector(Director director) {
        String sql = "INSERT INTO directors (name) VALUES (?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql, new String[]{"id"});
            stmt.setString(1, director.getName());
            return stmt;
        }, keyHolder);
        director.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        return getDirectorById(director.getId());
    }

    @Override
    public Director updateDirector(Director director) {
        String sql = "UPDATE directors SET name = ? WHERE id = ?";
        int count = jdbcTemplate.update(sql,
                director.getName(),
                director.getId());
        if (count == 0) {
            throw new IncorrectParamException("Неверный id!");
        }
        return getDirectorById(director.getId());
    }

    @Override
    public void dellDirectorById(long id) {
        String sqlFd = "DELETE FROM films_director WHERE director_id = ?";
        jdbcTemplate.update(sqlFd, id);
        String sql = "DELETE FROM directors WHERE id = ?";
        int count = jdbcTemplate.update(sql, id);
        if (count == 0) {
            throw new IncorrectParamException("Невереный id!");
        }
    }

    @Override
    public List<Director> getDirectors() {
        String sql = "SELECT * FROM directors";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeDirector(rs));
    }

    @Override
    public Director getDirectorById(long id) {
        String sql = "SELECT * FROM directors WHERE id = ?";
        List<Director> directors = jdbcTemplate.query(sql, (rs, rowNum) -> makeDirector(rs), id);
        if (directors.isEmpty()) {
            throw new IncorrectParamException("Неверный id!");
        }
        return directors.get(0);
    }

    private Director makeDirector(ResultSet rs) throws SQLException {
        return new Director(rs.getLong("id"), rs.getString("name"));
    }
}
