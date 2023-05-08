package ru.practicum.shareit.exceptions;

public class NotRightsException extends IllegalArgumentException {
    public NotRightsException(String message) {
        super(message);
    }
}
