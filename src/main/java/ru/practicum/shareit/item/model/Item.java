package ru.practicum.shareit.item.model;

import lombok.Data;


@Data
public class Item {
    private int id;
    private String name;
    private String description;
    private boolean available;
    private int owner;
    private int request;

    public static class Builder {
        private final Item newItem;

        public Builder() {
            newItem = new Item();
        }

        public Builder withName(String name) {
            newItem.name = name;
            return this;
        }

        public Builder withDescription(String description) {
            newItem.description = description;
            return this;
        }

        public Builder withAvailable(boolean available) {
            newItem.available = available;
            return this;
        }

        public Builder withOwner(int owner) {
            newItem.owner = owner;
            return this;
        }

        public Item build() {
            return newItem;
        }
    }
}
