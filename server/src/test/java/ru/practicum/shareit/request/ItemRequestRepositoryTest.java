package ru.practicum.shareit.request;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
class ItemRequestRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRequestRepository itemRequestRepository;

    User user1 = new User();
    User user2 = new User();
    ItemRequest itemRequest1 = new ItemRequest();
    ItemRequest itemRequest2 = new ItemRequest();
    Pageable pageable;

    @BeforeEach
    void setUp() {
        user1.setName("name1");
        user1.setEmail("name1@mail.ru");
        user1 = userRepository.save(user1);

        user2.setName("name2");
        user2.setEmail("name2@mail.ru");
        user2 = userRepository.save(user2);

        itemRequest1.setDescription("request_description");
        itemRequest1.setRequestor(user2);
        itemRequest1.setCreated(LocalDateTime.now());
        itemRequest1 = itemRequestRepository.save(itemRequest1);

        itemRequest2.setDescription("request_description2");
        itemRequest2.setRequestor(user1);
        itemRequest2.setCreated(LocalDateTime.now());
        itemRequest2 = itemRequestRepository.save(itemRequest2);

    }

    @AfterEach
    void tearDown() {
        itemRequestRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void findAllByRequestor_IdNot() {
        final Optional<List<ItemRequest>> itemRequests =
                Optional.ofNullable(itemRequestRepository
                        .findAllByRequestor_IdNot(2L, pageable));
        assertNotNull(itemRequests);
    }

    @Test
    void findAllByRequestor_IdOrderByCreatedAsc() {
        final Optional<List<ItemRequest>> itemRequests =
                Optional.ofNullable(itemRequestRepository
                        .findAllByRequestor_IdNot(1L, pageable));
        assertNotNull(itemRequests);
    }
}