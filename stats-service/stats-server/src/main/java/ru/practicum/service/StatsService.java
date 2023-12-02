package ru.practicum.service;

import ru.practicum.model.EndpointHitEntity;
import ru.practicum.model.ViewStats;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface StatsService {

    void save(EndpointHitEntity endpointHitEntity);

    List<ViewStats> findAllBetweenDates(LocalDateTime start,
                                        LocalDateTime end,
                                        Collection<String> uris,
                                        boolean isUniqueIps);
}
