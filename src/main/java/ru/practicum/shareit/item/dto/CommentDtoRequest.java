package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonPropertyOrder
@AllArgsConstructor
public class CommentDtoRequest {

    @NotBlank
    String text;

    LocalDateTime createTime = LocalDateTime.now();
}
