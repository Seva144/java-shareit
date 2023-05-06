package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingState;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
public class BookingController {

    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> createBooking(@Valid @RequestBody BookingDtoRequest bookingDtoRequest,
                                                @RequestHeader(value = "X-Sharer-User-Id") Long userId) {
        log.info("Создание бронирования пользователем userId={}", userId);
        return bookingClient.createBooking(userId, bookingDtoRequest);
    }

    @PatchMapping("/{idBooking}")
    public ResponseEntity<Object> patchBooking(@PathVariable("idBooking") Long idBooking,
                                               @RequestParam Boolean approved,
                                               @RequestHeader(value = "X-Sharer-User-Id") Long userId) {
        log.info("Обновление бронирования idBooking ={} пользователем userId={}", idBooking, userId);
        return bookingClient.patchBooking(idBooking, userId, approved);
    }

    @GetMapping("/{idBooking}")
    public ResponseEntity<Object> getBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                             @PathVariable Long idBooking) {
        log.info("Получить запись о бронировании idBooking ={} пользователем userId={}", idBooking, userId);
        return bookingClient.getBooking(userId, idBooking);
    }

    @GetMapping
    public ResponseEntity<Object> getAllUserBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                                    @RequestParam(name = "state", defaultValue = "all") String stateParam,
                                                    @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                    @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        BookingState state = BookingState.from(stateParam)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
        log.info("Получить все записи о бронировании пользователем userId={} cо статусом {}", userId, stateParam);
        return bookingClient.getAllUserBooking(userId, state, from, size);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getAllOwnerBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                                     @RequestParam(name = "state", defaultValue = "all") String stateParam,
                                                     @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                     @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        BookingState state = BookingState.from(stateParam)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
        log.info("Получить все записи о бронировании пользователем userId={} cо статусом {}", userId, stateParam);
        return bookingClient.getAllOwnerBooking(userId, state, from, size);
    }

}

