package ru.practicum.exception;


public class EventDateInPastException extends Exception {

    public EventDateInPastException(String message) {
        super(message);
    }
}
