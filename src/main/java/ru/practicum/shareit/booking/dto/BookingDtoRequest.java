package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;


import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonPropertyOrder
@AllArgsConstructor
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
