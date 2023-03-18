package ru.practicum.shareit.user.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Builder
@Data
public class User {
    private int id;
    private String name;

    @NotBlank
    @Email
    private String email;
}
