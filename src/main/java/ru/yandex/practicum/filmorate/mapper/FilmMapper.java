package ru.yandex.practicum.filmorate.mapper;

import org.mapstruct.Mapper;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.model.Film;

@Mapper(componentModel = "spring", uses = {DirectorSetMapper.class, GenreSetMapper.class})
public interface FilmMapper {
    Film mapToModel(FilmDto filmDto);

    FilmDto mapToDto(Film film);
}



