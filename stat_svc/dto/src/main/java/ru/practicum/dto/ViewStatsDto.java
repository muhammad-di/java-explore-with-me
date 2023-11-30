package ru.practicum.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ViewStatsDto {
    private final String app;
    private final String uri;
    private long hits;
}

