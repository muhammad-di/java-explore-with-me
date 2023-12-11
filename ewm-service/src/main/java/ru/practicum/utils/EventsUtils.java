package ru.practicum.utils;

import ru.practicum.dto.ViewStatsDto;
import ru.practicum.dto.events.EventShortDto;

import java.time.LocalDateTime;
import java.util.Comparator;

public class EventsUtils {
    private EventsUtils() {
    }

    public static Comparator<EventShortDto> getCompareByViews() {
        return (o1, o2) -> (int) (o1.getViews() - o2.getViews());
    }

    public static LocalDateTime getDefaultRangeStart() {
        return LocalDateTime.now();
    }

    public static long getDefaultEndpointHits() {
        return 0;
    }

    public static ViewStatsDto getDefaultViewStatsDto() {
        return ViewStatsDto.builder()
                .hits(0L)
                .build();
    }

    public static LocalDateTime getEndpointHitTimestamp() {
        return LocalDateTime.now();
    }

    public static String getAppName() {
        return "ewm-main-service";
    }

    public static LocalDateTime getDefaultStartForEndpointHit() {
        return LocalDateTime.of(2015, 1, 1, 0, 0);
    }

    public static LocalDateTime getDefaultEndForEndpointHit() {
        return LocalDateTime.of(2055, 1, 1, 0, 0);
    }


}