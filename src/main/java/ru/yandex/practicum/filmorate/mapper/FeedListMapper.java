package ru.yandex.practicum.filmorate.mapper;

import org.mapstruct.Mapper;
import ru.yandex.practicum.filmorate.dto.FeedDto;
import ru.yandex.practicum.filmorate.model.Feed;

import java.util.List;

@Mapper(componentModel = "spring", uses = FeedMapper.class)
public interface FeedListMapper {
    List<Feed> toModelList(List<FeedDto> feedDtos);

    List<FeedDto> toDTOList(List<Feed> feeds);
}
