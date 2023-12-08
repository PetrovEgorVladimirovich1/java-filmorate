package ru.yandex.practicum.filmorate.utils;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.DirectorDto;
import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class MappingUtils {

    public DirectorDto mapToDirectorDto(Director director) {
        DirectorDto dto = new DirectorDto();
        dto.setId(director.getId());
        dto.setName(director.getName());
        return dto;
    }

    public Director mapToDirector(DirectorDto dto) {
        Director director = new Director();
        director.setId(dto.getId());
        director.setName(dto.getName());
        return director;
    }

    public static <R, E> List<R> convertList(List<E> list, Function<E, R> converter) {
        return list.stream().map(e -> converter.apply(e)).collect(Collectors.toList());
    }
}
