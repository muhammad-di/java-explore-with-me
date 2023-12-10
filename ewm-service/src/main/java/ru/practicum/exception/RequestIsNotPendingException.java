package ru.practicum.exception;


public class RequestIsNotPendingException extends Exception {

    public RequestIsNotPendingException(String message) {
        super(message);
    }
}
