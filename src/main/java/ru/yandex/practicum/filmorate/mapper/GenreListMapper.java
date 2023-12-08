package ru.yandex.practicum.filmorate.mapper;

import org.mapstruct.Mapper;
import ru.yandex.practicum.filmorate.dto.GenreDto;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@Mapper(componentModel = "spring", uses = GenreMapper.class)
public interface GenreListMapper {
    List<Genre> toModelList(List<GenreDto> genreDtos);

    List<GenreDto> toDTOList(List<Genre> genres);
}
