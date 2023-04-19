package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.item.dto.ItemShort;
import ru.practicum.shareit.user.dto.UserShort;

import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@JsonPropertyOrder
@AllArgsConstructor
public class BookingDtoResponse {
    Long id;
    LocalDateTime start;
    LocalDateTime end;
    Status status;
    UserShort booker;
    ItemShort item;
}
