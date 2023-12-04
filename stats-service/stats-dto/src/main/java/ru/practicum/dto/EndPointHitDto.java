package ru.practicum.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EndPointHitDto {

    @NotBlank(message = "app is mandatory")
    private String app;

    @NotBlank(message = "uri is mandatory")
    private String uri;

    @NotBlank(message = "ip is mandatory")
    private String ip;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
    @NotNull(message = "timestamp is mandatory")
    private LocalDateTime timestamp;
}