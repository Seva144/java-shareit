package ru.practicum.shareit.request.model;


import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemRequest {
    int id;
    String description;
    int requestor;
    private LocalDateTime created;
}
