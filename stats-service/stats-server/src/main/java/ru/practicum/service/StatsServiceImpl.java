package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import ru.practicum.model.EndpointHitEntity;
import ru.practicum.model.ViewStats;
import ru.practicum.repository.EndpointHitRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;


@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {

    private final EndpointHitRepository repository;

    @Transactional
    @Override
    public void save(EndpointHitEntity endpointHitEntity) {
        repository.save(endpointHitEntity);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ViewStats> findAllBetweenDates(LocalDateTime start,
                                               LocalDateTime end,
                                               Collection<String> uris,
                                               boolean isUniqueIps) {
        if (isUniqueIps) {
            return repository.viewStatsForUrisUniqueIps(uris, start, end);
        }
        if (CollectionUtils.isEmpty(uris)) {
            return repository.viewStats(start, end);
        }
        return repository.viewStatsForUris(uris, start, end);
    }
}
