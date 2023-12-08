package ru.yandex.practicum.filmorate.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@Builder
public class DirectorDto {
    private long id;
    @NotBlank(message = "Имя не может быть пустым или null")
    private String name;
}
