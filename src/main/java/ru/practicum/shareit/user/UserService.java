package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userStorage) {
        this.userRepository = userStorage;
    }

    public List<UserDto> getAllUsers() {
        return userRepository.getAllUsers()
                .stream()
                .map(UserMapper::mapToDto)
                .collect(Collectors.toList());
    }

    public UserDto createUser(UserDto userDto) {
        validEmail(userDto.getEmail(), getAllUsers());
        User user = userRepository.createUser(UserMapper.mapToModel(userDto));
        return getUser(user.getId());
    }

    public UserDto patchUser(Long id, UserDto userDto) {
        validUser(id);
        List<UserDto> usersWithoutUserId = getAllUsers()
                .stream()
                .filter(user -> user.getId() != id)
                .collect(Collectors.toList());

        validEmail(userDto.getEmail(), usersWithoutUserId);

        if (userDto.getName() != null) userRepository.getUser(id).setName(userDto.getName());
        if (userDto.getEmail() != null) userRepository.getUser(id).setEmail(userDto.getEmail());

        return getUser(id);
    }

    public UserDto getUser(long id) {
        validUser(id);
        return UserMapper.mapToDto(userRepository.getUser(id));
    }

    public void deleteUser(long id) {
        validUser(id);
        userRepository.deleteUser(id);
    }

    public void validEmail(String email, List<UserDto> users) {
        if (users.stream().anyMatch(user -> Objects.equals(user.getEmail(), email)))
            throw new ValidationException("Пользователя с такой почтой не существует");
    }

    public void validUser(long id) {
        if (userRepository.getUser(id) == null) throw new NotFoundException("Пользователь не найден");
    }
}
