package ru.practicum.exception;


public class EventDateBeforeOneHourFromPublishingException extends Exception {

    public EventDateBeforeOneHourFromPublishingException(String message) {
        super(message);
    }
}
