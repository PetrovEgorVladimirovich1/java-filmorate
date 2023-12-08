package ru.yandex.practicum.filmorate.mapper;

import org.mapstruct.Mapper;
import ru.yandex.practicum.filmorate.dto.DirectorDto;
import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring", uses = DirectorMapper.class)
public interface DirectorSetMapper {
    Set<Director> toModeSet(Set<DirectorDto> directorDtos);
    Set<DirectorDto> toDTOSet(Set<Director> directors);
}
