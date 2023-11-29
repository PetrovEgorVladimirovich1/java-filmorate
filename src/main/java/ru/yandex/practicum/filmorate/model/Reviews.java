package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Reviews {
    private Long reviewId;
    private String content;
    private Integer isPositive;
    private Long userId;
    private Long filmId;
    private Integer useful;
}
