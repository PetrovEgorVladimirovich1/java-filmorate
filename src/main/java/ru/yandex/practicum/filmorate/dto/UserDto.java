package ru.yandex.practicum.filmorate.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@Data
@Builder
public class UserDto {
    private Integer id;
    @Email(message = "некорректный email")
    private String email;
    @NotBlank(message = "Логин не может быть пустым или null")
    private String login;
    private String name;
    @Past(message = "Дата рождения не может быть в будущем")
    private LocalDate birthday;
}