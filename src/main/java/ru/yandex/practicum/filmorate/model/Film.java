package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import ru.yandex.practicum.filmorate.annotations.IsBeforeBirthdayMovie;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@SuperBuilder
public class Film {

    private long id;

    @NotBlank
    private String name;

    @Size(max = 200)
    private String description;

    @NotNull
    @IsBeforeBirthdayMovie
    private LocalDate releaseDate;

    @Min(value = 1)
    private int duration;

    private Mpa mpa;

    private final Set<Genre> genres = new HashSet<>();

    private final Set<Long> likes = new HashSet<>();
}
