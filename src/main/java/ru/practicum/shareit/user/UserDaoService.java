package ru.practicum.shareit.user;

import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.mapper.UserMapperStruct;
import ru.practicum.shareit.user.model.User;

import java.util.*;
import java.util.stream.Collectors;

public class UserDaoService {

    private final UserDao userDao;

    public UserDaoService(UserDao userStorage) {
        this.userDao = userStorage;
    }

    public List<UserDto> getAllUsers() {
        return userDao.getAllUsers()
                .stream()
                .map(UserMapper::mapToDto)
                .collect(Collectors.toList());
    }

    public UserDto createUser(UserDto userDto) {
        validEmail(userDto.getEmail(), getAllUsers());
        User user = userDao.createUser(UserMapper.mapToModel(userDto));
        return getUser(user.getId());
    }

    public UserDto patchUser(Long id, UserDto userDto) {
        validUser(id);
        List<UserDto> usersWithoutUserId = getAllUsers()
                .stream()
                .filter(user -> user.getId() != id)
                .collect(Collectors.toList());

        validEmail(userDto.getEmail(), usersWithoutUserId);

        if (userDto.getName() != null) userDao.getUser(id).setName(userDto.getName());
        if (userDto.getEmail() != null) userDao.getUser(id).setEmail(userDto.getEmail());

        return getUser(id);
    }

    public UserDto getUser(long id) {
        validUser(id);
        return UserMapper.mapToDto(userDao.getUser(id));
    }

    public void deleteUser(long id) {
        validUser(id);
        userDao.deleteUser(id);
    }

    public void validEmail(String email, List<UserDto> users) {
        if (users.stream().anyMatch(user -> Objects.equals(user.getEmail(), email)))
            throw new ValidationException("Пользователя с такой почтой не существует");
    }

    public void validUser(long id) {
        if (userDao.getUser(id) == null) throw new NotFoundException("Пользователь не найден");
    }
}
