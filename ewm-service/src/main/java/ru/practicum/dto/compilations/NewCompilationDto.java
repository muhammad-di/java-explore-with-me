package ru.practicum.dto.compilations;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Collection;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewCompilationDto {
    private boolean pinned;
    @NotBlank
    @Size(min = 1, max = 50, message = "title should be more then 0 and less than 51 symbols")
    private String title;
    private Collection<Long> events;
}
