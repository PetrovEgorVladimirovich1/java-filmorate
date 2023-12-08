package ru.yandex.practicum.filmorate.mapper;

import org.mapstruct.Mapper;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.model.User;


@Mapper(componentModel = "spring", uses = UserListMapper.class)
public interface UserMapper {
    User mapToModel(UserDto userDto);
    UserDto mapToDto(User user);
}
