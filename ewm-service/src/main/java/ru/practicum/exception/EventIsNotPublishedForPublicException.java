package ru.practicum.exception;


public class EventIsNotPublishedForPublicException extends Exception {

    public EventIsNotPublishedForPublicException(String message) {
        super(message);
    }
}
