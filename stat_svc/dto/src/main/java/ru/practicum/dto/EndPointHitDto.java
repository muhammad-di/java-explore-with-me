package ru.practicum.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
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

    @PastOrPresent
    private LocalDateTime timestamp;
}
