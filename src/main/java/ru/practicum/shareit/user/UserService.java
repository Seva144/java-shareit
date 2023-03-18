package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.user.model.User;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {

    private final UserStorage userStorage;

    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public User createUser(User user) throws ValidationException {
        validEmail(user.getEmail(), getAllUsers());
        return userStorage.createUser(user);
    }

    public User patchUser(Integer id, Map<String, Object> fields) throws NotFoundException {
        List<User> usersWithoutUserId = getAllUsers()
                .stream()
                .filter(user -> user.getId() != id)
                .collect(Collectors.toList());

        User excitingUser = userStorage.getUser(id);
        fields.forEach((key, value) -> {
            if (Objects.equals(key, "email")) {
                try {
                    validEmail(String.valueOf(value), usersWithoutUserId);
                } catch (ValidationException e) {
                    throw new RuntimeException(e);
                }
            }
            Field field = ReflectionUtils.findField(User.class, key);
            Objects.requireNonNull(field).setAccessible(true);
            ReflectionUtils.setField(field, excitingUser, value);
        });
        return userStorage.getUser(id);
    }

    public User getUser(int id) throws NotFoundException {
        return userStorage.getUser(id);
    }

    public void deleteUser(int id) {
        userStorage.deleteUser(id);
    }

    public void validEmail(String email, List<User> users) throws ValidationException {
        if (users.stream().anyMatch(user -> Objects.equals(user.getEmail(), email))) {
            throw new ValidationException();
        }
    }
}
