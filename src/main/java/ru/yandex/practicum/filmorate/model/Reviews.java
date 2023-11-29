package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Reviews {
    private long reviewId;
    private String content;
    private boolean isPositive;
    private long userId;
    private long filmId;
    private int useful;
}
