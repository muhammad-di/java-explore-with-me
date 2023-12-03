package ru.practicum.mapper;

import ru.practicum.dto.EndPointHitDto;
import ru.practicum.model.EndpointHitEntity;


public class EndPointHitMapper {
    public static EndpointHitEntity toEndPointHitEntity(EndPointHitDto dto) {
        return EndpointHitEntity.builder()
                .app(dto.getApp())
                .uri(dto.getUri())
                .ip(dto.getIp())
                .hitTime(dto.getTimestamp())
                .build();
    }
}
