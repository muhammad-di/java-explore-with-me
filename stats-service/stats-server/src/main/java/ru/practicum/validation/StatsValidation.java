package ru.practicum.validation;

import ru.practicum.exception.StartAfterEndException;

import java.time.LocalDateTime;

public class StatsValidation {
    public static Boolean validateStartAndEnd(LocalDateTime start, LocalDateTime end) throws StartAfterEndException {
        return validateStartBeforeEnd(start, end);
    }

    public static Boolean validateStartBeforeEnd(LocalDateTime start, LocalDateTime end) throws StartAfterEndException {
        if (start.isBefore(end)) {
            return true;
        } else {
            String message = String.format("start { %s } is before end { %s }", start, end);
            throw new StartAfterEndException(message);
        }
    }
}

