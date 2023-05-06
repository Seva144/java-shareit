package ru.practicum.shareit.item;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRequestRepository itemRequestRepository;


    User user = new User();
    Item item = new Item();
    ItemRequest itemRequest = new ItemRequest();

    @BeforeEach
    void start() {

        user.setName("name1");
        user.setEmail("name1@mail.ru");
        user = userRepository.save(user);

        itemRequest.setDescription("request_description");
        itemRequest.setRequestor(user);
        itemRequest.setCreated(LocalDateTime.now());
        itemRequestRepository.save(itemRequest);

        item.setName("item");
        item.setDescription("description");
        item.setAvailable(true);
        item.setOwner(user.getId());
        item.setRequestId(1);

    }

    @AfterEach
    void tearDown() {
        itemRepository.deleteAll();
        userRepository.deleteAll();
        itemRequestRepository.deleteAll();
    }

    @Test
    void getAllByRequestId() {
        final Optional<List<Item>> items =
                Optional.ofNullable(itemRepository.getAllByRequestId(1L));
        assertNotNull(items);
    }

    @Test
    void findAllByOwnerOrderByIdAsc() {
        item = itemRepository.save(item);
        Pageable pageable = PageRequest.of(1, 10);
        final Optional<List<Item>> items =
                Optional.ofNullable(itemRepository.findAllByOwnerOrderByIdAsc(item.getOwner(), pageable));
        assertNotNull(items);
    }


}