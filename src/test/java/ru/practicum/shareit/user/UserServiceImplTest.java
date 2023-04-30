package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.transaction.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserServiceImplTest {

    private final EntityManager em;
    private final UserService service;
    UserDto userDto = new UserDto();

    @Test
    void createUser() {
        userDto.setName("user1");
        userDto.setEmail("user1@mail.ru");
        service.createUser(userDto);

        Query query = em.createNativeQuery("Select * from users where users.email=?", User.class);
        query.setParameter(1, "user1@mail.ru");

        User user1 = (User) query.getSingleResult();

        assertThat(user1.getId(), notNullValue());
        assertThat(user1.getName(), equalTo(userDto.getName()));
        assertThat(user1.getEmail(), equalTo(userDto.getEmail()));
    }

    @Test
    void getAllUsers() {
        userDto.setName("user1");
        userDto.setEmail("user1@mail.ru");
        service.createUser(userDto);

        List<UserDto> users = service.getAllUsers();
        Query query = em.createNativeQuery("Select * from users", User.class);

        List<User> userList = query.getResultList();
        List<UserDto> userDto = userList
                .stream()
                .map(UserMapper::mapToDto)
                .collect(Collectors.toList());

        assertNotNull(userList);
        assertEquals(userList.size(), 1);
        assertThat(users, equalTo(userDto));

    }

    @Test
    void getUser() {
        userDto.setName("user1");
        userDto.setEmail("user1@mail.ru");
        service.createUser(userDto);

        Query query = em.createNativeQuery("Select * from users where users.email=?", User.class);
        query.setParameter(1, "user1@mail.ru");

        User user1 = (User) query.getSingleResult();

        assertThat(user1.getId(), notNullValue());
        assertThat(user1.getName(), equalTo(userDto.getName()));
        assertThat(user1.getEmail(), equalTo(userDto.getEmail()));
    }


    @Test
    void deleteUser() {
        userDto.setName("user1");
        userDto.setEmail("user1@mail.ru");
        UserDto user = service.createUser(userDto);

        service.deleteUser(user.getId());
        Query query = em.createNativeQuery("Select * from users where users.id=?", User.class);
        query.setParameter(1, "1");

        assertThrows(NoResultException.class, query::getSingleResult);
    }

    @Test
    void patchUser() {
        userDto.setName("user1");
        userDto.setEmail("user1@mail.ru");
        UserDto user = service.createUser(userDto);

        UserDto userDtoUpdate = new UserDto();
        userDtoUpdate.setName("user1_update");
        userDtoUpdate.setEmail("user1_update@mail.ru");
        service.patchUser(user.getId(), userDtoUpdate);
        Query query = em.createNativeQuery("Select * from users where users.id=?", User.class);
        query.setParameter(1, user.getId());

        User user1 = (User) query.getSingleResult();

        assertThat(user1.getId(), notNullValue());
        assertThat(user1.getName(), equalTo(userDtoUpdate.getName()));
        assertThat(user1.getEmail(), equalTo(userDtoUpdate.getEmail()));

    }

    @Test
    void validUser() {
        assertThrows(NotFoundException.class, () -> service.getUser(2L));
    }
}