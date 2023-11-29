package ru.yandex.practicum.filmorate.storage.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Reviews;
import ru.yandex.practicum.filmorate.storage.dal.ReviewsStorage;

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
        return null;
    }

    @Override
    public Reviews update(Reviews reviews) {
        return null;
    }

    @Override
    public Reviews remove(Long id) {
        return null;
    }

    @Override
    public Reviews get(Long id) {
        return null;
    }

    @Override
    public List<Reviews> getReviewsFilm(Long id, Long count) {
        return null;
    }

    @Override
    public Reviews addLike(Long id, Long userId) {
        return null;
    }

    @Override
    public Reviews addDislike(Long id, Long userId) {
        return null;
    }

    @Override
    public Reviews removeLike(Long id, Long userId) {
        return null;
    }

    @Override
    public Reviews removeDislike(Long id, Long userId) {
        return null;
    }
}
