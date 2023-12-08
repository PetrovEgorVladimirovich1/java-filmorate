package ru.yandex.practicum.filmorate.mapper;

import org.mapstruct.Mapper;
import ru.yandex.practicum.filmorate.dto.ReviewDto;
import ru.yandex.practicum.filmorate.model.Reviews;

@Mapper(componentModel = "spring")
public interface ReviewMapper {
    Reviews mapToModel(ReviewDto reviewDto);

    ReviewDto mapToDto(Reviews reviews);
}
