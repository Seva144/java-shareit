package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.NotRightsException;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class BookingServiceImplTest {

    private final EntityManager em;
    private final ItemService itemService;
    private final UserService userService;
    private final BookingService bookingService;

    ItemDto itemDto = new ItemDto();
    UserDto userDto1 = new UserDto();
    UserDto userDto2 = new UserDto();
    BookingDtoRequest bookingDtoRequest = new BookingDtoRequest();


    @BeforeEach
    void setUp() {
        userDto1.setName("name1");
        userDto1.setEmail("user1@mail.ru");
        userDto1 = userService.createUser(userDto1);
        userDto2.setName("name2");
        userDto2.setEmail("user2@mail.ru");
        userDto2 = userService.createUser(userDto2);
        itemDto.setName("item1");
        itemDto.setDescription("description1");
        itemDto.setAvailable(true);
        itemDto = itemService.createItem(userDto1.getId(), itemDto);

        bookingDtoRequest.setStart(LocalDateTime.now().minusHours(3));
        bookingDtoRequest.setEnd(LocalDateTime.now().minusHours(2));
        bookingDtoRequest.setItemId(itemDto.getId());
        bookingDtoRequest.setUserId(userDto2.getId());
    }

    @Test
    void createBooking() {
        BookingDtoResponse bookingDtoResponse = bookingService.createBooking(bookingDtoRequest, bookingDtoRequest.getUserId());

        Query query = em.createNativeQuery("select * from booking where booking.id=?", Booking.class);
        query.setParameter(1, bookingDtoResponse.getId());
        Booking booking = (Booking) query.getSingleResult();

        assertThat(booking.getId(), notNullValue());
        assertThat(booking.getId(), equalTo(bookingDtoResponse.getId()));
        assertThat(booking.getStatus(), equalTo(bookingDtoResponse.getStatus()));
    }

    @Test
    void getBooking() {
        BookingDtoResponse bookingCreate = bookingService.createBooking(bookingDtoRequest, bookingDtoRequest.getUserId());
        BookingDtoResponse bookingDtoResponse = bookingService.getBooking(userDto2.getId(), bookingCreate.getId());

        Query query = em.createNativeQuery("select * from booking where booking.id=?", Booking.class);
        query.setParameter(1, bookingDtoResponse.getId());
        Booking booking = (Booking) query.getSingleResult();

        assertThat(booking.getId(), notNullValue());
        assertThat(booking.getId(), equalTo(bookingDtoResponse.getId()));
        assertThat(booking.getStatus(), equalTo(bookingDtoResponse.getStatus()));

    }

    @Test
    void patchBooking_addApprovedStatus() {
        BookingDtoResponse bookingCreate = bookingService.createBooking(bookingDtoRequest, bookingDtoRequest.getUserId());
        BookingDtoResponse bookingDtoResponse = bookingService.patchBooking(bookingCreate.getId(), true, userDto1.getId());

        Query query = em.createNativeQuery("select * from booking where booking.id=?1", Booking.class);
        query.setParameter(1, bookingDtoResponse.getId());
        Booking booking = (Booking) query.getSingleResult();

        assertThat(booking.getStatus(), equalTo(bookingDtoResponse.getStatus()));

    }

    @Test
    void patchBooking_addRejectedStatus() {
        BookingDtoResponse bookingCreate = bookingService.createBooking(bookingDtoRequest, bookingDtoRequest.getUserId());
        BookingDtoResponse bookingDtoResponse = bookingService.patchBooking(bookingCreate.getId(), false, userDto1.getId());

        Query query = em.createNativeQuery("select * from booking where booking.id=?1", Booking.class);
        query.setParameter(1, bookingDtoResponse.getId());
        Booking booking = (Booking) query.getSingleResult();

        assertThat(booking.getStatus(), equalTo(bookingDtoResponse.getStatus()));

    }

    @Test
    void getAllUserBooking_stateAll() {
        bookingService.createBooking(bookingDtoRequest, bookingDtoRequest.getUserId());
        List<BookingDtoResponse> bookingDtoResponse = bookingService.getAllUserBooking(userDto2.getId(), "ALL", 0, 10);

        Query query = em.createNativeQuery("select * from booking", Booking.class);
        List<Booking> booking = query.getResultList();

        assertNotNull(booking);
        assertEquals(booking.size(), 1);
        assertThat(booking.get(0).getId(), equalTo(bookingDtoResponse.get(0).getId()));
        assertThat(booking.get(0).getStatus(), equalTo(bookingDtoResponse.get(0).getStatus()));

    }

    @Test
    void getAllUserBooking_stateFuture() {
        bookingDtoRequest.setStart(LocalDateTime.now().plusHours(2));
        bookingDtoRequest.setEnd(LocalDateTime.now().plusHours(4));
        bookingService.createBooking(bookingDtoRequest, bookingDtoRequest.getUserId());
        List<BookingDtoResponse> bookingDtoResponse = bookingService.getAllUserBooking(userDto2.getId(), "FUTURE", 0, 10);

        Query query = em.createNativeQuery("select * from booking where booking.start_time > ?1", Booking.class);
        query.setParameter(1, LocalDateTime.now());
        List<Booking> booking = query.getResultList();

        assertNotNull(booking);
        assertEquals(booking.size(), 1);
        assertThat(booking.get(0).getId(), equalTo(bookingDtoResponse.get(0).getId()));
        assertThat(booking.get(0).getStatus(), equalTo(bookingDtoResponse.get(0).getStatus()));
    }

    @Test
    void getAllUserBooking_stateWaiting() {
        bookingService.createBooking(bookingDtoRequest, bookingDtoRequest.getUserId());
        List<BookingDtoResponse> bookingDtoResponse = bookingService.getAllUserBooking(userDto2.getId(), "WAITING", 0, 10);

        Query query = em.createNativeQuery("select * from booking where booking.status = ?1", Booking.class);
        query.setParameter(1, "WAITING");
        List<Booking> booking = query.getResultList();

        assertNotNull(booking);
        assertEquals(booking.size(), 1);
        assertThat(booking.get(0).getId(), equalTo(bookingDtoResponse.get(0).getId()));
        assertThat(booking.get(0).getStatus(), equalTo(bookingDtoResponse.get(0).getStatus()));

    }

    @Test
    void getAllUserBooking_stateCurrent() {
        bookingDtoRequest.setStart(LocalDateTime.now().minusHours(2));
        bookingDtoRequest.setEnd(LocalDateTime.now().plusHours(4));
        bookingService.createBooking(bookingDtoRequest, bookingDtoRequest.getUserId());
        List<BookingDtoResponse> bookingDtoResponse = bookingService.getAllUserBooking(userDto2.getId(), "CURRENT", 0, 10);

        Query query = em.createNativeQuery("select * from booking where booking.start_time < ?1 " +
                "and booking.end_time>?2", Booking.class);
        query.setParameter(1, LocalDateTime.now());
        query.setParameter(2, LocalDateTime.now());
        List<Booking> booking = query.getResultList();

        assertNotNull(booking);
        assertEquals(booking.size(), 1);
        assertThat(booking.get(0).getId(), equalTo(bookingDtoResponse.get(0).getId()));
        assertThat(booking.get(0).getStatus(), equalTo(bookingDtoResponse.get(0).getStatus()));
    }

    @Test
    void getAllUserBooking_statePast() {
        bookingDtoRequest.setStart(LocalDateTime.now().minusHours(3));
        bookingDtoRequest.setEnd(LocalDateTime.now().minusHours(2));
        bookingService.createBooking(bookingDtoRequest, bookingDtoRequest.getUserId());
        List<BookingDtoResponse> bookingDtoResponse = bookingService.getAllUserBooking(userDto2.getId(), "PAST", 0, 10);

        Query query = em.createNativeQuery("select * from booking where booking.end_time < ?1 ", Booking.class);
        query.setParameter(1, LocalDateTime.now());
        List<Booking> booking = query.getResultList();

        assertNotNull(booking);
        assertEquals(booking.size(), 1);
        assertThat(booking.get(0).getId(), equalTo(bookingDtoResponse.get(0).getId()));
        assertThat(booking.get(0).getStatus(), equalTo(bookingDtoResponse.get(0).getStatus()));
    }

    @Test
    void getAllUserBooking_stateRejected() {
        BookingDtoResponse bookingCreate = bookingService.createBooking(bookingDtoRequest, bookingDtoRequest.getUserId());
        bookingService.patchBooking(bookingCreate.getId(), false, userDto1.getId());
        List<BookingDtoResponse> bookingDtoResponse = bookingService.getAllUserBooking(userDto2.getId(), "REJECTED", 0, 10);

        Query query = em.createNativeQuery("select * from booking where booking.status =?1 ", Booking.class);
        query.setParameter(1, "REJECTED");
        List<Booking> booking = query.getResultList();

        assertNotNull(booking);
        assertEquals(booking.size(), 1);
        assertThat(booking.get(0).getId(), equalTo(bookingDtoResponse.get(0).getId()));
        assertThat(booking.get(0).getStatus(), equalTo(bookingDtoResponse.get(0).getStatus()));
    }

    @Test
    void getAllOwnerBooking_stateAll() {
        bookingService.createBooking(bookingDtoRequest, bookingDtoRequest.getUserId());
        List<BookingDtoResponse> bookingDtoResponse = bookingService.getAllOwnerBooking(userDto1.getId(), "ALL", 0, 10);

        Query query = em.createNativeQuery("select * from booking", Booking.class);
        List<Booking> booking = query.getResultList();

        assertNotNull(booking);
        assertEquals(booking.size(), 1);
        assertThat(booking.get(0).getId(), equalTo(bookingDtoResponse.get(0).getId()));
        assertThat(booking.get(0).getStatus(), equalTo(bookingDtoResponse.get(0).getStatus()));
    }

    @Test
    void getAllOwnerBooking_stateFuture() {
        bookingDtoRequest.setStart(LocalDateTime.now().plusHours(2));
        bookingDtoRequest.setEnd(LocalDateTime.now().plusHours(4));
        bookingService.createBooking(bookingDtoRequest, bookingDtoRequest.getUserId());
        List<BookingDtoResponse> bookingDtoResponse = bookingService.getAllOwnerBooking(userDto1.getId(), "FUTURE", 0, 10);

        Query query = em.createNativeQuery("select * from booking where booking.start_time > ?1", Booking.class);
        query.setParameter(1, LocalDateTime.now());
        List<Booking> booking = query.getResultList();

        assertNotNull(booking);
        assertEquals(booking.size(), 1);
        assertThat(booking.get(0).getId(), equalTo(bookingDtoResponse.get(0).getId()));
        assertThat(booking.get(0).getStatus(), equalTo(bookingDtoResponse.get(0).getStatus()));
    }

    @Test
    void getAllOwnerBooking_stateWaiting() {
        bookingService.createBooking(bookingDtoRequest, bookingDtoRequest.getUserId());
        List<BookingDtoResponse> bookingDtoResponse = bookingService.getAllOwnerBooking(userDto1.getId(), "WAITING", 0, 10);

        Query query = em.createNativeQuery("select * from booking where booking.status = ?1", Booking.class);
        query.setParameter(1, "WAITING");
        List<Booking> booking = query.getResultList();

        assertNotNull(booking);
        assertEquals(booking.size(), 1);
        assertThat(booking.get(0).getId(), equalTo(bookingDtoResponse.get(0).getId()));
        assertThat(booking.get(0).getStatus(), equalTo(bookingDtoResponse.get(0).getStatus()));

    }

    @Test
    void getAllOwnerBooking_stateCurrent() {
        bookingDtoRequest.setStart(LocalDateTime.now().minusHours(2));
        bookingDtoRequest.setEnd(LocalDateTime.now().plusHours(4));
        bookingService.createBooking(bookingDtoRequest, bookingDtoRequest.getUserId());
        List<BookingDtoResponse> bookingDtoResponse = bookingService.getAllOwnerBooking(userDto1.getId(), "CURRENT", 0, 10);

        Query query = em.createNativeQuery("select * from booking where booking.start_time < ?1 " +
                "and booking.end_time>?2", Booking.class);
        query.setParameter(1, LocalDateTime.now());
        query.setParameter(2, LocalDateTime.now());
        List<Booking> booking = query.getResultList();

        assertNotNull(booking);
        assertEquals(booking.size(), 1);
        assertThat(booking.get(0).getId(), equalTo(bookingDtoResponse.get(0).getId()));
        assertThat(booking.get(0).getStatus(), equalTo(bookingDtoResponse.get(0).getStatus()));
    }

    @Test
    void getAllOwnerBooking_statePast() {
        bookingDtoRequest.setStart(LocalDateTime.now().minusHours(3));
        bookingDtoRequest.setEnd(LocalDateTime.now().minusHours(2));
        bookingService.createBooking(bookingDtoRequest, bookingDtoRequest.getUserId());
        List<BookingDtoResponse> bookingDtoResponse = bookingService.getAllOwnerBooking(userDto1.getId(), "PAST", 0, 10);

        Query query = em.createNativeQuery("select * from booking where booking.end_time < ?1 ", Booking.class);
        query.setParameter(1, LocalDateTime.now());
        List<Booking> booking = query.getResultList();

        assertNotNull(booking);
        assertEquals(booking.size(), 1);
        assertThat(booking.get(0).getId(), equalTo(bookingDtoResponse.get(0).getId()));
        assertThat(booking.get(0).getStatus(), equalTo(bookingDtoResponse.get(0).getStatus()));
    }

    @Test
    void getAllOwnerBooking_stateRejected() {
        BookingDtoResponse bookingCreate = bookingService.createBooking(bookingDtoRequest, bookingDtoRequest.getUserId());
        bookingService.patchBooking(bookingCreate.getId(), false, userDto1.getId());
        List<BookingDtoResponse> bookingDtoResponse = bookingService.getAllOwnerBooking(userDto1.getId(), "REJECTED", 0, 10);

        Query query = em.createNativeQuery("select * from booking where booking.status =?1 ", Booking.class);
        query.setParameter(1, "REJECTED");
        List<Booking> booking = query.getResultList();

        assertNotNull(booking);
        assertEquals(booking.size(), 1);
        assertThat(booking.get(0).getId(), equalTo(bookingDtoResponse.get(0).getId()));
        assertThat(booking.get(0).getStatus(), equalTo(bookingDtoResponse.get(0).getStatus()));
    }

    @Test
    void validUser() {
        assertThrows(NotFoundException.class, () ->
                bookingService.getAllUserBooking(99L, "WAITING", 1, 10));
    }


    @Test
    void validPage() {
        assertThrows(NotRightsException.class, () ->
                bookingService.getAllUserBooking(userDto1.getId(), "WAITING", -1, 10));
    }
}