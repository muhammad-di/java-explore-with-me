package ru.practicum.exception;


public class RequesterAndInitiatorAreSameException extends Exception {

    public RequesterAndInitiatorAreSameException(String message) {
        super(message);
    }
}
