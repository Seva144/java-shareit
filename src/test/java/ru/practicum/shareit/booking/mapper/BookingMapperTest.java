package ru.practicum.shareit.booking.mapper;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.dto.BookingDtoShort;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ItemDtoShort;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserShort;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class BookingMapperTest {

    User user = new User
            (0L, "user1", "user1@mail.ru");
    Item item = new Item
            (0L, "item1", "description1", true, 0L, 0L);
    UserShort userShort = new UserShort();
    ItemDtoShort itemDtoShort = new ItemDtoShort();
    BookingDtoRequest bookingDtoRequest = new BookingDtoRequest(LocalDateTime.now(), LocalDateTime.now(), 0L, 0L);
    BookingDtoResponse bookingDtoResponse = new BookingDtoResponse(0L, LocalDateTime.now(), LocalDateTime.now(), Status.WAITING, userShort, itemDtoShort);
    Booking booking = new Booking(0L, LocalDateTime.now(), LocalDateTime.now(), Status.WAITING, user, item);

    @BeforeEach
    void setUp() {
        userShort.setId(0L);
        itemDtoShort.setId(0L);
        itemDtoShort.setName("item1");
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void mapToModel() {
        Booking bookingActual = BookingMapper.mapToModel(bookingDtoRequest, user, item);
        assertEquals(bookingActual.getUser(), user);
        assertEquals(bookingActual.getItem(), item);
        assertEquals(bookingActual.getStart(), bookingDtoRequest.getStart());
        assertEquals(bookingActual.getEnd(), bookingDtoRequest.getEnd());
    }

    @Test
    void mapToDto() {
        BookingDtoResponse bookingActual = BookingMapper.mapToDto(booking);
        assertEquals(bookingActual.getId(), bookingDtoResponse.getId());
        assertEquals(bookingActual.getStatus(), bookingDtoResponse.getStatus());
        assertEquals(bookingActual.getBooker(), userShort);
        assertEquals(bookingActual.getBooker().getId(), userShort.getId());
        assertEquals(bookingActual.getItem().getId(), itemDtoShort.getId());
        assertEquals(bookingActual.getItem().getName(), itemDtoShort.getName());
    }

    @Test
    void mapToShort() {
        BookingDtoShort bookingActual = BookingMapper.mapToShort(booking);
        assertEquals(0L, bookingActual.getId());
        assertEquals(0L, bookingActual.getBookerId());
    }
}