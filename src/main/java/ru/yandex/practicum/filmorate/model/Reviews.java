package ru.yandex.practicum.filmorate.model;

import lombok.Data;


@Data
public class Reviews {
    private Long reviewId;
    private String content;
    private Boolean isPositive;
    private Long userId;
    private Long filmId;
    private Integer useful = 0;

    public Reviews(String content, Boolean isPositive, Long filmId, Long userId) {
        this.content = content;
        this.isPositive = isPositive;
        this.userId = userId;
        this.filmId = filmId;
    }
}
