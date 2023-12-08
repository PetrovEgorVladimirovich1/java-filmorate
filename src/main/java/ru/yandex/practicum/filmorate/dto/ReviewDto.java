package ru.yandex.practicum.filmorate.dto;

import lombok.*;

import javax.validation.constraints.NotNull;

@Data
@Builder
public class ReviewDto {
    private Long reviewId;
    @NotNull
    private String content;
    @NotNull
    private Boolean isPositive;
    @NotNull
    private Long userId;
    @NotNull
    private Long filmId;
    private Integer useful = 0;
}
