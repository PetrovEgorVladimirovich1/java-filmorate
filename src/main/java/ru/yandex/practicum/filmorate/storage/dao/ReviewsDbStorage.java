package ru.yandex.practicum.filmorate.storage.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Reviews;
import ru.yandex.practicum.filmorate.storage.dal.ReviewsStorage;

import javax.validation.ValidationException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class ReviewsDbStorage implements ReviewsStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ReviewsDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Reviews create(Reviews reviews) {
        jdbcTemplate.update(
                "INSERT INTO Reviews (content, isPositive, userId, filmId, useFul) VALUES (?, ?, ?, ?, ?)",
                reviews.getContent(),
                reviews.getIsPositive(),
                reviews.getUserId(),
                reviews.getFilmId(),
                reviews.getUseful()
        );
        return reviews;
    }

    @Override
    public Reviews update(Reviews reviews) {
        if (get(reviews.getReviewId()) == null) {
            throw new ValidationException("Not found key: " + reviews.getReviewId());
        }
        jdbcTemplate.update(
                "UPDATE Reviews SET content = ?, isPositive = ?, userId = ?, filmId = ?, useFul = ? WHERE id = ?",
                reviews.getContent(),
                reviews.getIsPositive(),
                reviews.getUserId(),
                reviews.getFilmId(),
                reviews.getUseful(),
                reviews.getReviewId()
        );
        return reviews;
    }

    @Override
    public Integer remove(Long id) {
        return jdbcTemplate.update("DELETE FROM Reviews WHERE id = ?", id);
    }

    @Override
    public Reviews get(Long id) {
        return jdbcTemplate.queryForObject("SELECT * FROM Reviews WHERE id = ?", this::mapRow, id);
    }

    @Override
    public List<Reviews> getReviewsFilm(Long filmId, Long count) {
        String sql = "SELECT * FROM Reviews "
                + (filmId == null ? "" : "WHERE filmid = " + filmId)
                + (count == null ? " LIMIT = 10" : " LIMIT = " + count + ";");
        return this.jdbcTemplate.query(sql, this::mapRow);
    }

    @Override
    public Reviews addLike(Long id, Long userId) {
        this.jdbcTemplate.
                update("INSERT INTO useful (reviewId, userId, useFul) VALUES (?, ?, ?)",
                        id,
                        userId,
                        1);
        return get(id);
    }

    @Override
    public Reviews addDislike(Long id, Long userId) {
        this.jdbcTemplate.
                update("INSERT INTO useful (reviewId, userId, useFul) VALUES (?, ?, ?)",
                        id,
                        userId,
                        -1);
        return get(id);
    }

    @Override
    public Reviews removeLike(Long id, Long userId) {
        jdbcTemplate.update("DELETE FROM useful WHERE reviewId = ? AND userId = ?", id, userId);
        return get(id);
    }

    @Override
    public Reviews removeDislike(Long id, Long userId) {
        jdbcTemplate.update("DELETE FROM useful WHERE reviewId = ? AND userId = ?", id, userId);
        return get(id);
    }

    private Reviews mapRow(ResultSet rs, int rowNum) throws SQLException {
        List<Integer> useFul = jdbcTemplate.query("SELECT useFul FROM useful WHERE reviewId = ?",
                (o1, o2) -> o1.getInt("useful"), rs.getInt("id"));
        Integer number = 0;
        for (Integer a:useFul) {
            number += a;
        }
        return new Reviews(
                rs.getLong("id"),
                rs.getString("contennt"),
                rs.getInt("ispositive"),
                rs.getLong("userid"),
                rs.getLong("filmid"),
                number);
    }
}
