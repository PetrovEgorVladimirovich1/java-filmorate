package ru.yandex.practicum.filmorate.mapper;

import org.mapstruct.Mapper;
import ru.yandex.practicum.filmorate.dto.MpaDto;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

@Mapper(componentModel = "spring", uses = MpaMapper.class)
public interface MpaListMapper {

    List<Mpa> toModelList(List<MpaDto> mpaDtos);

    List<MpaDto> toDTOList(List<Mpa> mpas);
}
