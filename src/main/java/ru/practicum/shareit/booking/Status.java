package ru.practicum.shareit.booking;

public enum Status {
    // - новое бронирование, ожидает одобрения
    WAITING,
    // - бронирование подтверждено
    APPROVED,
    // - бронирование отклонено
    REJECTED,
    // - бронирование отменено
    CANCELED
}
