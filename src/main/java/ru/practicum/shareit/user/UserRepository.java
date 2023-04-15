package ru.practicum.shareit.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.user.model.User;


public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "select u.id from users as u where id=?1",
            nativeQuery = true)
    Long validUser(Long id);

}
