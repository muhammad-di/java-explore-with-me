package ru.practicum.exception;


public class ParticipationLimitReachedException extends Exception {

    public ParticipationLimitReachedException(String message) {
        super(message);
    }
}
