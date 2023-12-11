package ru.practicum.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.practicum.dto.errors.ApiError;
import ru.practicum.exception.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {
    // INTERNAL_SERVER_ERROR ///////////////////////////////////////////////////////////////////////////////////////////
    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleThrowable(final Throwable e) {
        return new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server exception", e.getMessage());
    }

    // BAD_REQUEST /////////////////////////////////////////////////////////////////////////////////////////////////////
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiError handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ApiError(HttpStatus.BAD_REQUEST, "Incorrectly made request.", errors);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleValidationExceptions(MissingServletRequestParameterException e) {
        return new ApiError(HttpStatus.BAD_REQUEST, "Incorrectly made request.", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleMethodArgumentTypeMismatchException(final MethodArgumentTypeMismatchException e) {
        return new ApiError(HttpStatus.BAD_REQUEST, "Incorrectly made request.", e.getMessage());
    }

    //    *
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleEventDateInPastException(final EventDateInPastException e) {
        return new ApiError(HttpStatus.BAD_REQUEST,
                "For the requested operation the conditions are not met.",
                e.getMessage());
    }

    //    *
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleEventDateBeforeTwoHoursException(final EventDateBeforeTwoHoursException e) {
        return new ApiError(HttpStatus.BAD_REQUEST,
                "For the requested operation the conditions are not met.",
                e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleIncorrectInitiatorException(final IncorrectInitiatorException e) {
        return new ApiError(HttpStatus.BAD_REQUEST,
                "For the requested operation the conditions are not met.",
                e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleRequesterAndClaimedRequesterAreNotSameException(final RequesterAndClaimedRequesterAreNotSameException e) {
        return new ApiError(HttpStatus.BAD_REQUEST,
                "For the requested operation the conditions are not met.",
                e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleRangeStartIsAfterRangeEndException(final RangeStartIsAfterRangeEndException e) {
        return new ApiError(HttpStatus.BAD_REQUEST,
                "For the requested operation the conditions are not met.",
                e.getMessage());
    }

    // CONFLICT ////////////////////////////////////////////////////////////////////////////////////////////////////////
    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleDataIntegrityViolationException(final DataIntegrityViolationException e) {
        return new ApiError(HttpStatus.CONFLICT, "Integrity constraint has been violated.", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleEventIsAlreadyPublishedException(final EventIsAlreadyPublishedException e) {
        return new ApiError(HttpStatus.CONFLICT,
                "For the requested operation the conditions are not met.",
                e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleEventIsAlreadyCanceledException(final EventIsAlreadyCanceledException e) {
        return new ApiError(HttpStatus.CONFLICT,
                "For the requested operation the conditions are not met.",
                e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleRequestAlreadyExistsException(final RequestAlreadyExistsException e) {
        return new ApiError(HttpStatus.CONFLICT,
                "Integrity constraint has been violated.",
                e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleRequesterAndInitiatorAreSameException(final RequesterAndInitiatorAreSameException e) {
        return new ApiError(HttpStatus.CONFLICT,
                "Integrity constraint has been violated.",
                e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleEventIsNotPublishedException(final EventIsNotPublishedException e) {
        return new ApiError(HttpStatus.CONFLICT,
                "Integrity constraint has been violated.",
                e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleParticipationLimitReachedException(final ParticipationLimitReachedException e) {
        return new ApiError(HttpStatus.CONFLICT,
                "Integrity constraint has been violated.",
                e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleRequestIsNotPendingException(final RequestIsNotPendingException e) {
        return new ApiError(HttpStatus.CONFLICT,
                "Integrity constraint has been violated.",
                e.getMessage());
    }

    // NOT_FOUND ///////////////////////////////////////////////////////////////////////////////////////////////////////
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleCategoryNotFoundException(final CategoryNotFoundException e) {
        return new ApiError(HttpStatus.NOT_FOUND, "The required category was not found.", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleUserNotFoundException(final UserNotFoundException e) {
        return new ApiError(HttpStatus.NOT_FOUND, "The required user was not found.", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleUserEventNotFoundException(final EventNotFoundException e) {
        return new ApiError(HttpStatus.NOT_FOUND, "The required event was not found.", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleEventIsNotPublishedForPublicException(final EventIsNotPublishedForPublicException e) {
        return new ApiError(HttpStatus.NOT_FOUND, "The required event was not found.", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleCompilationNotFoundException(final CompilationNotFoundException e) {
        return new ApiError(HttpStatus.NOT_FOUND, "The required event was not found.", e.getMessage());
    }

    // FORBIDDEN ///////////////////////////////////////////////////////////////////////////////////////////////////////
}
