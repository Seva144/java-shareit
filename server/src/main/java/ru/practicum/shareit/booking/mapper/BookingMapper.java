package ru.practicum.shareit.booking.mapper;

import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.dto.BookingDtoShort;
import ru.practicum.shareit.item.dto.ItemDtoShort;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserShort;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.booking.model.Booking;

public class BookingMapper {

    public static Booking mapToModel(BookingDtoRequest bookingDto, User user, Item item) {
        Booking booking = new Booking();
        booking.setUser(user);
        booking.setItem(item);
        booking.setStart(bookingDto.getStart());
        booking.setEnd(bookingDto.getEnd());
        return booking;
    }

    public static BookingDtoResponse mapToDto(Booking booking) {
        Long idUser = booking.getUser().getId();
        Long idItem = booking.getItem().getId();
        String nameItem = booking.getItem().getName();
        return BookingDtoResponse.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .status(booking.getStatus())
                .booker(UserShort.builder()
                        .id(idUser)
                        .build())
                .item(ItemDtoShort.builder()
                        .id(idItem)
                        .name(nameItem)
                        .build())
                .build();
    }

    public static BookingDtoShort mapToShort(Booking booking) {
        return BookingDtoShort.builder()
                .id(booking.getId())
                .bookerId(booking.getUser().getId())
                .build();
    }

}
