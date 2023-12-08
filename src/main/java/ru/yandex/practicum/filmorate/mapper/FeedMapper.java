package ru.yandex.practicum.filmorate.mapper;

import org.mapstruct.Mapper;
import ru.yandex.practicum.filmorate.dto.FeedDto;
import ru.yandex.practicum.filmorate.model.Feed;

@Mapper(componentModel = "spring")
public interface FeedMapper {
    Feed mapToModel(FeedDto feedDto);

    FeedDto mapToDto(Feed feed);
}

