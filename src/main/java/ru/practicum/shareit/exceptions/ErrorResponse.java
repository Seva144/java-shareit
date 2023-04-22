package ru.practicum.shareit.exceptions;


public class ErrorResponse {
    String error;
    String description;
    String cause;

    public ErrorResponse(String error, String description, Throwable cause) {
        this.error = error;
        this.description = description;
        this.cause = String.valueOf(cause);
    }

    public ErrorResponse(String error, String description) {
        this.error = error;
        this.description = description;
    }

    public ErrorResponse(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }

    public String getDescription() {
        return description;
    }

    public String getCause() {
        return cause;
    }


}
