package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@JsonPropertyOrder
public class CommentDtoResponse {

    Long id;
    String text;
    String authorName;
    LocalDateTime created;

}
