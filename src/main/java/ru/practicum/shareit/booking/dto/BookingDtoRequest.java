package ru.practicum.shareit.booking.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingDtoRequest {
    @NotNull
    @Future
    LocalDateTime start;
    @Future
    @NotNull
    LocalDateTime end;
    Long userId;
    Long itemId;
}
