package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.lang.reflect.Field;
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
        List<UserDto> users = new ArrayList<>();
        userRepository
                .getAllUsers()
                .forEach(user -> users.add(UserMapper.mapToDto(user)));
        return users;
    }

    public UserDto createUser(UserDto userDto) throws ValidationException, NotFoundException {
        validEmail(userDto.getEmail(), getAllUsers());
        User user = userRepository.createUser(UserMapper.mapToModel(userDto));
        return getUser(user.getId());
    }

    public UserDto patchUser(Long id, UserDto userDto) throws ValidationException, NotFoundException {

        List<UserDto> usersWithoutUserId = getAllUsers()
                .stream()
                .filter(user -> user.getId() != id)
                .collect(Collectors.toList());

        validEmail(userDto.getEmail(), usersWithoutUserId);

        Map<String, Object> fields = new HashMap<>();
        if (userDto.getName() != null) fields.put("name", userDto.getName());
        if (userDto.getEmail() != null) fields.put("email", userDto.getEmail());

        User excitingUser = userRepository.getUser(id);
        fields.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(User.class, key);
            Objects.requireNonNull(field).setAccessible(true);
            ReflectionUtils.setField(field, excitingUser, value);
        });
        User user = userRepository.getUser(id);
        return UserMapper.mapToDto(user);
    }

    public UserDto getUser(long id) throws NotFoundException {
        User user = userRepository.getUser(id);
        return UserMapper.mapToDto(user);
    }

    public void deleteUser(long id) {
        userRepository.deleteUser(id);
    }

    public void validEmail(String email, List<UserDto> users) throws ValidationException {
        if (users.stream().anyMatch(user -> Objects.equals(user.getEmail(), email))) {
            throw new ValidationException();
        }
    }
}
