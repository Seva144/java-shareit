package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
@Slf4j
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public BookingDtoResponse createBooking(@Valid @RequestBody BookingDtoRequest bookingDto,
                                            @RequestHeader(value = "X-Sharer-User-Id") Long userId) {
        return bookingService.createBooking(bookingDto, userId);
    }

    @PatchMapping("/{idBooking}")
    public BookingDtoResponse patchBooking(@PathVariable("idBooking") Long idBooking,
                                           @RequestParam Boolean approved,
                                           @RequestHeader(value = "X-Sharer-User-Id") Long userId) {

        return bookingService.patchBooking(idBooking, approved, userId);
    }


    @GetMapping("/{idBooking}")
    public BookingDtoResponse getBooking(@PathVariable("idBooking") Long idBooking,
                                         @RequestHeader(value = "X-Sharer-User-Id") Long userId) {
        return bookingService.getBooking(userId, idBooking);
    }


    @GetMapping
    public List<BookingDtoResponse> getAllUserBooking(@RequestHeader(value = "X-Sharer-User-Id") Long userId,
                                                      @RequestParam(defaultValue = "ALL") String state) {
        return bookingService.getAllUserBooking(userId, state);
    }


    @GetMapping("/owner")
    public List<BookingDtoResponse> getAllOwnerBooking(@RequestHeader(value = "X-Sharer-User-Id") Long ownerId,
                                                       @RequestParam(defaultValue = "ALL") String state) {
        return bookingService.getAllOwnerBooking(ownerId, state);
    }

}
