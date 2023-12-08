package ru.yandex.practicum.filmorate.mapper;

import org.mapstruct.Mapper;
import ru.yandex.practicum.filmorate.dto.GenreDto;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring", uses = GenreMapper.class)
public interface GenreSetMapper {

    Set<Genre> toModelSet(Set<GenreDto> genreDtos);
    Set<GenreDto> toDTOSet(Set<Genre> genres);
}

