package ru.practicum.shareit.booking;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.ItemRepository;
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
class BookingRepositoryTest {

    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRequestRepository itemRequestRepository;

    User user1 = new User();
    User user2 = new User();
    Item item = new Item();
    ItemRequest itemRequest = new ItemRequest();
    Booking booking = new Booking();
    Pageable pageable;

    @BeforeEach
    void setUp() {
        user1.setName("name1");
        user1.setEmail("name1@mail.ru");
        user1 = userRepository.save(user1);

        user2.setName("name2");
        user2.setEmail("name2@mail.ru");
        user2 = userRepository.save(user2);

        itemRequest.setDescription("request_description");
        itemRequest.setRequestor(user2);
        itemRequest.setCreated(LocalDateTime.now());
        itemRequest = itemRequestRepository.save(itemRequest);

        item.setName("item");
        item.setDescription("description");
        item.setAvailable(true);
        item.setOwner(user1.getId());
        item.setRequestId(itemRequest.getId());
        itemRepository.save(item);

        booking.setStart(LocalDateTime.now().plusHours(1));
        booking.setEnd(LocalDateTime.now().plusHours(3));
        booking.setStatus(Status.WAITING);
        booking.setUser(user2);
        booking.setItem(item);
        bookingRepository.save(booking);

    }

    @AfterEach
    void tearDown() {
        bookingRepository.deleteAll();
        itemRequestRepository.deleteAll();
        itemRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void getFirstByItemIdAndStartBeforeOrderByStartDesc() {
        item = itemRepository.save(item);

        final Optional<Booking> bookings =
                Optional.ofNullable(bookingRepository
                        .getFirstByItemIdAndStartBeforeOrderByStartDesc(2L, LocalDateTime.now()));
        assertNotNull(bookings);
    }

    @Test
    void findTopByItemIdAndStartAfterAndEndAfterOrderByStartAsc() {
        final Optional<Booking> bookings =
                Optional.ofNullable(bookingRepository
                        .findTopByItemIdAndStartAfterAndEndAfterOrderByStartAsc
                                (1L, LocalDateTime.now(), LocalDateTime.now()));
        assertNotNull(bookings);
    }

    @Test
    void findAllByUserIdAndStatus() {
        final Optional<List<Booking>> items =
                Optional.ofNullable(bookingRepository
                        .findAllByUserIdAndStatus(2L, Status.WAITING, pageable));
        assertNotNull(items);
    }

    @Test
    void findAllByUserIdOrderByStartDesc() {
        final Optional<List<Booking>> items =
                Optional.ofNullable(bookingRepository
                        .findAllByUserIdOrderByStartDesc(2L, pageable));
        assertNotNull(items);
    }

    @Test
    void findAllByUserIdAndStartIsAfterOrderByStartDesc() {
        final Optional<List<Booking>> items =
                Optional.ofNullable(bookingRepository
                        .findAllByUserIdAndStartIsAfterOrderByStartDesc
                                (2L, LocalDateTime.now(), pageable));
        assertNotNull(items);
    }

    @Test
    void findAllByUserIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc() {
        final Optional<List<Booking>> items =
                Optional.ofNullable(bookingRepository
                        .findAllByUserIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc
                                (2L, LocalDateTime.now(), LocalDateTime.now(), pageable));
        assertNotNull(items);
    }

    @Test
    void findAllByUserIdAndEndIsBeforeOrderByStartDesc() {
        final Optional<List<Booking>> items =
                Optional.ofNullable(bookingRepository
                        .findAllByUserIdAndEndIsBeforeOrderByStartDesc
                                (2L, LocalDateTime.now(), pageable));
        assertNotNull(items);
    }

    @Test
    void findAllByItem_OwnerAndStatus() {
        final Optional<List<Booking>> items =
                Optional.ofNullable(bookingRepository
                        .findAllByItem_OwnerAndStatus(2L, Status.WAITING, pageable));
        assertNotNull(items);
    }

    @Test
    void findAllByItem_OwnerOrderByStartDesc() {
        final Optional<List<Booking>> items =
                Optional.ofNullable(bookingRepository
                        .findAllByItem_OwnerOrderByStartDesc(2L, pageable));
        assertNotNull(items);
    }

    @Test
    void findAllByItem_OwnerAndEndIsBeforeOrderByStartDesc() {
        final Optional<List<Booking>> items =
                Optional.ofNullable(bookingRepository
                        .findAllByItem_OwnerAndEndIsBeforeOrderByStartDesc
                                (2L, LocalDateTime.now(), pageable));
        assertNotNull(items);
    }

    @Test
    void findAllByItem_OwnerAndStartIsBeforeAndEndIsAfterOrderByStartDesc() {
        final Optional<List<Booking>> items =
                Optional.ofNullable(bookingRepository
                        .findAllByItem_OwnerAndStartIsBeforeAndEndIsAfterOrderByStartDesc
                                (2L, LocalDateTime.now(), LocalDateTime.now(), pageable));
        assertNotNull(items);
    }

    @Test
    void findAllByItem_OwnerAndStartIsAfterOrderByStartDesc() {
        final Optional<List<Booking>> items =
                Optional.ofNullable(bookingRepository
                        .findAllByItem_OwnerAndStartIsAfterOrderByStartDesc
                                (2L, LocalDateTime.now(), pageable));
        assertNotNull(items);
    }

    @Test
    void findFirstByItemIdAndUserIdAndStatusAndEndIsBefore() {
        final Optional<Booking> items =
                Optional.ofNullable(bookingRepository
                        .findFirstByItemIdAndUserIdAndStatusAndEndIsBefore
                                (item.getId(), user2.getId(), Status.WAITING, LocalDateTime.now()));
        assertNotNull(items);
    }
}