package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapperStruct;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    @Override
    public List<UserDto> getAllUsers() {
        return repository.findAll()
                .stream()
                .map(UserMapperStruct.INSTANCE::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto patchUser(Long id, UserDto userDto) {
        validUser(id);
        User user = repository.getReferenceById(id);

        if (userDto.getName() != null) user.setName(userDto.getName());
        if (userDto.getEmail() != null) user.setEmail(userDto.getEmail());

        repository.save(user);

        return getUser(id);
    }

    @Override
    public UserDto getUser(Long id) {
        validUser(id);
        return UserMapperStruct.INSTANCE.mapToDto(repository.getReferenceById(id));
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        User user = repository.save(UserMapperStruct.INSTANCE.mapToModel(userDto));
        return getUser(user.getId());
    }

    @Override
    public void deleteUser(Long id) {
        validUser(id);
        repository.deleteById(id);
    }

    public void validUser(Long id) {
        Optional.ofNullable(repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден")));
    }
}
