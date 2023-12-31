package ru.practicum;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.exception.StartAfterEndException;
import ru.practicum.model.ApiError;

import java.util.HashMap;
import java.util.Map;


@RestControllerAdvice
public class ErrorHandler {

    //    Unknown Error exception handler
    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleThrowable(final Throwable e) {
        return new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server exception", e.getMessage());
    }

    //    RequestParam exceptions handler
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleValidationExceptions(MissingServletRequestParameterException e) {
        return new ApiError(HttpStatus.BAD_REQUEST, "Incorrect request parameter", e.getMessage());
    }

    //    RequestBody exceptions handler
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

    //    BAD_REQUEST
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleStartAfterEndException(final StartAfterEndException e) {
        e.setErrorCode(HttpStatus.BAD_REQUEST);
        return new ApiError(HttpStatus.BAD_REQUEST, "Incorrect start and end parameter", e.getMessage());
    }
}
