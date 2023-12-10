package ru.practicum.mapper;

import ru.practicum.dto.ViewStatsDto;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;

public class ViewStatsMapper {
    public static Collection<ViewStatsDto> toViewStatsDtoList(Object objectList) {
        Collection<LinkedHashMap<String, Object>> list = (Collection<LinkedHashMap<String, Object>>) objectList;
        return list.stream()
                .map(ViewStatsMapper::toViewStatsDto)
                .collect(Collectors.toList());
    }

    public static ViewStatsDto toViewStatsDto(LinkedHashMap<String, Object> map) {
        return ViewStatsDto.builder()
                .app(getApp(map))
                .uri(getUri(map))
                .hits(getHits(map))
                .build();
    }

    private static String getApp(LinkedHashMap<String, Object> map) {
        Object appObject = map.get("app");
        String app = String.valueOf(appObject);
        return app;
    }

    private static String getUri(LinkedHashMap<String, Object> map) {
        Object uriObject = map.get("uri");
        String uri = String.valueOf(uriObject);
        return uri;
    }

    private static Long getHits(LinkedHashMap<String, Object> map) {
        Object hitsObject = map.get("hits");
        String stringToConvert = String.valueOf(hitsObject);
        Long hits = Long.parseLong(stringToConvert);
        return hits;
    }
}
