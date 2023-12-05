package ru.practicum.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.practicum.dto.errors.ApiError;
import ru.practicum.exception.CategoryNotFoundException;
import ru.practicum.exception.UserNotFoundException;

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

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleMethodArgumentTypeMismatchException(final MethodArgumentTypeMismatchException e) {
        return new ApiError(HttpStatus.BAD_REQUEST, "Incorrectly made request.", e.getMessage());
    }

    // CONFLICT ////////////////////////////////////////////////////////////////////////////////////////////////////////
    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleDataIntegrityViolationException(final DataIntegrityViolationException e) {
        return new ApiError(HttpStatus.CONFLICT, "Integrity constraint has been violated.", e.getMessage());
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
}
