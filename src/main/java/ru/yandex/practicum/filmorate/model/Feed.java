package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Feed {

    private long eventId;

    private long userId;

    private long entityId;

    private String eventType;

    private String operation;

    private long timestamp;
}
