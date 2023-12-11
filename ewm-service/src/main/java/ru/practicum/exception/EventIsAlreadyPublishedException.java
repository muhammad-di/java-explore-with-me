package ru.practicum.exception;


public class EventIsAlreadyPublishedException extends Exception {

    public EventIsAlreadyPublishedException(String message) {
        super(message);
    }
}
