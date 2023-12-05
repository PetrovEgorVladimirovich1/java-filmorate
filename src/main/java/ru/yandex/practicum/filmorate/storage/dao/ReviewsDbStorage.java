

package ru.yandex.practicum.filmorate.storage.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.IncorrectParamException;
import ru.yandex.practicum.filmorate.model.Reviews;
import ru.yandex.practicum.filmorate.storage.dal.ReviewsStorage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.List;
import java.util.Objects;


@Component
public class ReviewsDbStorage implements ReviewsStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ReviewsDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Reviews create(Reviews reviews) {
        String sql = "INSERT INTO Reviews (content, isPositive, userId, filmId, useFul) VALUES (?, ?, ?, ?, ?)";
        String sqlFeed = "INSERT INTO feeds (user_id, entity_id, event_type, operation, times) " +
                "VALUES (?, ?, 'REVIEW', 'ADD', ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                con -> {
                    PreparedStatement statement = con.prepareStatement(sql, new String[]{"id"});
                    statement.setString(1, reviews.getContent());
                    statement.setInt(2, reviews.getIsPositive() ? 1 : 0);
                    statement.setLong(3, reviews.getUserId());
                    statement.setLong(4, reviews.getFilmId());
                    statement.setInt(5, 0);
                    return statement;
                }, keyHolder
        );
        reviews.setReviewId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        jdbcTemplate.update(sqlFeed, reviews.getUserId(), reviews.getFilmId(), Instant.now());
        return reviews;
    }

    @Override
    public Reviews update(Reviews reviews) {
        String sqlFeed = "INSERT INTO feeds (user_id, entity_id, event_type, operation, times) " +
                "VALUES (?, ?, 'REVIEW', 'UPDATE', ?)";
        jdbcTemplate.update(
                "UPDATE Reviews SET content = ?, isPositive = ? WHERE id = ?",
                reviews.getContent(),
                reviews.getIsPositive() ? 1 : 0,
                reviews.getReviewId()
        );
        jdbcTemplate.update(sqlFeed, reviews.getUserId(), reviews.getFilmId(), Instant.now());
        return get(reviews.getReviewId());
    }

    @Override
    public Reviews remove(Long id) {
        Reviews reviews = get(id);
        String sqlFeed = "INSERT INTO feeds (user_id, entity_id, event_type, operation, times) " +
                "VALUES (?, ?, 'REVIEW', 'REMOVE', ?)";
        jdbcTemplate.update("DELETE FROM useful WHERE reviewId = ?", id);
        jdbcTemplate.update("DELETE FROM Reviews WHERE id = ?", id);
        jdbcTemplate.update(sqlFeed, reviews.getUserId(), reviews.getFilmId(), Instant.now());
        return reviews;
    }

    @Override
    public Reviews get(Long id) {
        List<Reviews> reviewsList = jdbcTemplate.query("SELECT * FROM Reviews WHERE id = ?", this::mapRow, id);
        if (reviewsList.isEmpty()) {
            throw new IncorrectParamException("Неверный id!");
        }
        return reviewsList.get(0);
    }

    @Override
    public List<Reviews> getReviewsFilm(Long filmId, Long count) {
        String sql = "SELECT * FROM Reviews"
                + (filmId == null ? "" : " WHERE filmid = " + filmId)
                + (count == null ? " LIMIT 10;" : " LIMIT " + count + ";");
        return this.jdbcTemplate.query(sql, this::mapRow);
    }

    @Override
    public Reviews addLike(Long id, Long userId) {
        String sqlFeed = "INSERT INTO feeds (user_id, entity_id, event_type, operation, times) " +
                "VALUES (?, ?, 'LIKE', 'ADD', ?)";
        this.jdbcTemplate.update("INSERT INTO useful(reviewId, userId, useFul) VALUES (?, ?, ?)",
                        id,
                        userId,
                        1);
        jdbcTemplate.update(sqlFeed, userId, id, Instant.now());
        return get(id);
    }

    @Override
    public Reviews addDislike(Long id, Long userId) {
        this.jdbcTemplate.update("INSERT INTO useful (reviewId, userId, useFul) VALUES (?, ?, ?)",
                        id,
                        userId,
                        -1);
        return get(id);
    }

    @Override
    public Reviews removeLike(Long id, Long userId) {
        String sqlFeed = "INSERT INTO feeds (user_id, entity_id, event_type, operation, times) " +
                "VALUES (?, ?, 'LIKE', 'REMOVE', ?)";
        jdbcTemplate.update("DELETE FROM useful WHERE reviewId = ? AND userId = ?", id, userId);
        jdbcTemplate.update(sqlFeed, userId, id, Instant.now());
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
        for (Integer a : useFul) {
            number += a;
        }
        Reviews reviews = new Reviews(
                rs.getString("content"),
                rs.getInt("ispositive") == 1,
                rs.getLong("filmid"),
                rs.getLong("userid")
        );
        reviews.setUseful(number);
        reviews.setReviewId(rs.getLong("id"));
        return reviews;
    }
}
