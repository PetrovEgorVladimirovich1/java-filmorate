package ru.yandex.practicum.filmorate.mapper;

import org.mapstruct.Mapper;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

@Mapper(componentModel = "spring", uses = FilmMapper.class)
public interface FilmListMapper {
    List<Film> toModelList(List<FilmDto> filmDtos);

    List<FilmDto> toDTOList(List<Film> films);
}

