package ru.practicum.exception;


public class RangeStartIsAfterRangeEndException extends Exception {

    public RangeStartIsAfterRangeEndException(String message) {
        super(message);
    }
}
