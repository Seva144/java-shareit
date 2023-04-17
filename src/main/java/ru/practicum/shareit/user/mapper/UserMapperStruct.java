package ru.practicum.shareit.user.mapper;

import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.mapstruct.Mapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserShort;
import ru.practicum.shareit.user.model.User;

@Mapper(componentModel = "spring")
public interface UserMapperStruct {

    UserMapperStruct INSTANCE = Mappers.getMapper(UserMapperStruct.class);

    @Mapping(target = "id", ignore = true)
    User mapToModel(UserDto userDto);

    UserDto mapToDto(User user);

    UserShort mapToShort(User user);

}
