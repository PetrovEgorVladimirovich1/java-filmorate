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
}

