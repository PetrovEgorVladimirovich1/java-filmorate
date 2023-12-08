package ru.yandex.practicum.filmorate.dto;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.yandex.practicum.filmorate.annotations.IsBeforeBirthdayMovie;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

@Data
@Builder
public class FilmDto {
    private Long id;
    @NotBlank(message = "Название не может быть пустым")
    private String name;
    @Size(max = 200, message = "Максимальная длина описания 200 символов")
    private String description;
    @IsBeforeBirthdayMovie
    @NotNull
    private LocalDate releaseDate;
    @Positive(message = "Некорректная продолжительность фильма")
    private Integer duration;
    @EqualsAndHashCode.Exclude
    private final Set<Long> likes = new HashSet<>();
    @EqualsAndHashCode.Exclude
    private Set<GenreDto> genres;
    @EqualsAndHashCode.Exclude
    private Set<DirectorDto> directors;
    @NotNull
    @EqualsAndHashCode.Exclude
    private Mpa mpa;

  /*  public Set<Long> getLikes() {
        return likes != null ? likes : new HashSet<>();
    }


   */
    public Set<GenreDto> getGenres() {
        return genres != null ? genres : new HashSet<>();
    }

    public Set<DirectorDto> getDirectors() {
        return directors != null ? directors : new HashSet<>();}
}
