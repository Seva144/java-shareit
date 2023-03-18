package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.lang.NonNull;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Data
public class ItemDto {
    private int id;
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    @NotNull
    private Boolean available;

    public static class Builder {
        private final ItemDto newItemDto;

        public Builder() {
            newItemDto = new ItemDto();
        }

        public ItemDto.Builder withId(int id) {
            newItemDto.id = id;
            return this;
        }

        public ItemDto.Builder withName(String name) {
            newItemDto.name = name;
            return this;
        }

        public ItemDto.Builder withDescription(String description) {
            newItemDto.description = description;
            return this;
        }

        public ItemDto.Builder withAvailable(boolean available) {
            newItemDto.available = available;
            return this;
        }

        public ItemDto build() {
            return newItemDto;
        }

    }
}
