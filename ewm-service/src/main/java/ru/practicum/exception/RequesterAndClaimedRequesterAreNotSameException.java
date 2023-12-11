package ru.practicum.exception;


public class RequesterAndClaimedRequesterAreNotSameException extends Exception {

    public RequesterAndClaimedRequesterAreNotSameException(String message) {
        super(message);
    }
}
