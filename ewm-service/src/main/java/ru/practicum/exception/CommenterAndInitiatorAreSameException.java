package ru.practicum.exception;


public class CommenterAndInitiatorAreSameException extends Exception {

    public CommenterAndInitiatorAreSameException(String message) {
        super(message);
    }
}
