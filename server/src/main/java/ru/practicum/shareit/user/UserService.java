package ru.practicum.shareit.user;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

@Service
public interface UserService {

    List<UserDto> getAllUsers();

    UserDto createUser(UserDto userDto);

    UserDto patchUser(Long id, UserDto user);

    UserDto getUser(Long id);

    void deleteUser(Long id);

}
