package ru.yandex.practicum.filmorate.mapper;

import org.mapstruct.Mapper;
import ru.yandex.practicum.filmorate.dto.DirectorDto;
import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;

@Mapper(componentModel = "spring", uses = DirectorMapper.class)
public interface DirectorListMapper {
    List<Director> toModelList(List<DirectorDto> directorDtos);

    List<DirectorDto> toDTOList(List<Director> directors);
}
