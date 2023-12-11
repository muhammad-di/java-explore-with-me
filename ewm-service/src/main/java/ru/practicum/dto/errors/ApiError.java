package ru.practicum.dto.errors;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
public class ApiError {
    private final HttpStatus status;
    private final String reason;
    private final Object message;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
    private final LocalDateTime timestamp;

    public ApiError(HttpStatus status, String reason, Object message) {
        this.status = status;
        this.reason = reason;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }
}
