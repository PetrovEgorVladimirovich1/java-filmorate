package ru.yandex.practicum.filmorate.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;


@Data
@AllArgsConstructor
@SuperBuilder
public class Reviews {

    private Long reviewId;
    @NotNull
    private String content;
    @NotNull
    private Boolean isPositive;
    private Long userId;
    private Long filmId;
    private Integer useful = 0;
/*
    public Reviews(String content, Boolean isPositive, Long filmId, Long userId) {
        this.content = content;
        this.isPositive = isPositive;
        this.userId = userId;
        this.filmId = filmId;
    }


    public Reviews(Long reviewId, String content, Boolean isPositive, Long filmId, Long userId) {
        this.reviewId = reviewId;
        this.content = content;
        this.isPositive = isPositive;
        this.userId = userId;
        this.filmId = filmId;
    }


 */

}

