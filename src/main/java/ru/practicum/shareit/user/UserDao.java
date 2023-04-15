package ru.practicum.shareit.user;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserDao {

    List<User> getAllUsers();

    User createUser(User user);

    User getUser(long id);

    void deleteUser(long id);

}
