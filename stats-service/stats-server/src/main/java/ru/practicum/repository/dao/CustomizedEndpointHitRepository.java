package ru.practicum.repository.dao;

import ru.practicum.model.ViewStats;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface CustomizedEndpointHitRepository {
    List<ViewStats> viewStatsForUrisUniqueIps(Collection<String> uris, LocalDateTime start, LocalDateTime end);
}
