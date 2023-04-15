package ru.practicum.shareit.booking;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;

import java.util.List;


@Service
public interface BookingService {

    BookingDtoResponse createBooking(BookingDtoRequest bookingDto, Long userId);

    BookingDtoResponse patchBooking(Long idBooking, Boolean approved, Long userId);

    BookingDtoResponse getBooking(Long idUser, Long idBooking);

    List<BookingDtoResponse> getAllUserBooking(Long idUser, String state);

    List<BookingDtoResponse> getAllOwnerBooking(Long idUser, String state);


}
