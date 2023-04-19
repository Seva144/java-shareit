package ru.practicum.shareit.user.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonPropertyOrder
@AllArgsConstructor
public class UserDto {
    long id;
    String name;

    @NotBlank
    @Email
    String email;
}
