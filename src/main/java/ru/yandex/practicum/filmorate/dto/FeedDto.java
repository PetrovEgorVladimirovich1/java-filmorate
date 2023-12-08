package ru.yandex.practicum.filmorate.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@Builder
public class FeedDto {
    @NotNull
    private int userId;

    private String eventType;

    private String operation;

    @NotNull
    private int eventId;

    @NotNull
    private int entityId;

    private Long timestamp;
}