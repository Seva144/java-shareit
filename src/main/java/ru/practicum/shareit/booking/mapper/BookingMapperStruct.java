package ru.practicum.shareit.booking.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.dto.BookingShort;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

@Mapper(componentModel = "spring")
public interface BookingMapperStruct {

    BookingMapperStruct INSTANCE = Mappers.getMapper(BookingMapperStruct.class);

    @Mapping(target = "id", ignore = true)
    Booking mapToModel(BookingDtoRequest bookingDto, User user, Item item);

    @Mapping(source = "booking.user.id", target = "booker.id")
    @Mapping(source = "booking.item.id", target = "item.id")
    @Mapping(source = "booking.item.name", target = "item.name")
    BookingDtoResponse mapToDto(Booking booking);

    @Mapping(source = "user.id", target = "bookerId")
    BookingShort mapToShort(Booking booking);

}
