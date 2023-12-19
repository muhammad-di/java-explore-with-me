package ru.practicum.exception;


public class CommentOwnerAndClaimToBeOwnerUserAreDifferentException extends Exception {

    public CommentOwnerAndClaimToBeOwnerUserAreDifferentException(String message) {
        super(message);
    }
}
