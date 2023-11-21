package ru.yandex.practicum.filmorate.storage.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.IncorrectParamException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.dal.MpaStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class MpaDbStorage implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Mpa> getMpa() {
        String sql = "SELECT * FROM mpa";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeMpa(rs));
    }

    @Override
    public Mpa getByIdMpa(long id) {
        String sql = "SELECT * FROM mpa WHERE id = ?";
        List<Mpa> mpa = jdbcTemplate.query(sql, (rs, rowNum) -> makeMpa(rs), id);
        if (mpa.isEmpty()) {
            throw new IncorrectParamException("Неверный id!");
        }
        return mpa.get(0);
    }

    private Mpa makeMpa(ResultSet rs) throws SQLException {
        return new Mpa(rs.getLong("id"), rs.getString("name"));
    }
}
