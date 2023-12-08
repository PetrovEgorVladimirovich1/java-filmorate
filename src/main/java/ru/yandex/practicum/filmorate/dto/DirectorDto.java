package ru.yandex.practicum.filmorate.dto;

import lombok.Data;

import javax.validation.constraints.Positive;

@Data
public class DirectorDto {
    @Positive
    Long id;
    String name;
}
