package ru.yandex.practicum.filmorate.model;

import lombok.Data;


@Data
public class Reviews {
    private Long reviewId;
    private String content;
    private Integer isPositive;
    private Long userId;
    private Long filmId;
    private Integer useful = 0;

    public Reviews(String content, Integer isPositive, Long userId, Long filmId) {
        this.content = content;
        this.isPositive = isPositive;
        this.userId = userId;
        this.filmId = filmId;
    }
}
