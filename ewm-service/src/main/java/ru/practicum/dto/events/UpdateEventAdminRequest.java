package ru.practicum.dto.events;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.model.EventStateAdminAction;

import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEventAdminRequest {
    @Size(min = 20, max = 2000, message = "annotation should be more then 19 and less than 2001 symbols")
    private String annotation;

    @Positive
    private Long category;

    @Size(min = 20, max = 7000, message = "description should be more then 19 and less than 7001 symbols")
    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
    private LocalDateTime eventDate;

    private Location location;

    private Boolean paid;

    private Long participantLimit;

    private Boolean requestModeration;

    private EventStateAdminAction stateAction;

    @Size(min = 3, max = 120, message = "title should be more then 2 and less than 121 symbols")
    private String title;
}
