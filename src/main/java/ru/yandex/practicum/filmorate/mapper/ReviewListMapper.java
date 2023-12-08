package ru.yandex.practicum.filmorate.mapper;

import org.mapstruct.Mapper;
import ru.yandex.practicum.filmorate.dto.ReviewDto;
import ru.yandex.practicum.filmorate.model.Reviews;

import java.util.List;

@Mapper(componentModel = "spring", uses = ReviewMapper.class)
public interface ReviewListMapper {

    List<Reviews> toModelList(List<ReviewDto> reviewDtos);

    List<ReviewDto> toDTOList(List<Reviews> reviewsList);
}