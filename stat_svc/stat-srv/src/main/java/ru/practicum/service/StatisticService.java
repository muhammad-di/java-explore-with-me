package ru.practicum.service;

import ru.practicum.model.EndpointHitEntity;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface StatisticService {
    void save(EndpointHitEntity endpointHitEntity);

    List<EndpointHitEntity> findAllBetweenDates(LocalDateTime start, LocalDateTime end, Collection<String> uris, boolean isUniqueIps);

}
