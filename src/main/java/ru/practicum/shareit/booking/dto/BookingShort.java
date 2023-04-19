package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonPropertyOrder
public class BookingShort {

    Long id;
    Long bookerId;

}
