package ru.practicum.exception;


public class EventDateBeforeTwoHoursException extends Exception {

    public EventDateBeforeTwoHoursException(String message) {
        super(message);
    }
}
