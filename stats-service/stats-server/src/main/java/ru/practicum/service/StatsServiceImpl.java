package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import ru.practicum.exception.StartAfterEndException;
import ru.practicum.model.EndpointHitEntity;
import ru.practicum.model.ViewStats;
import ru.practicum.repository.EndpointHitRepository;
import ru.practicum.validation.StatsValidation;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {

    private final EndpointHitRepository repository;

    @Transactional
    @Override
    public void save(EndpointHitEntity endpointHitEntity) {
        log.info("Register end point hit endPointHit = {}", endpointHitEntity);
        repository.save(endpointHitEntity);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ViewStats> findAllBetweenDates(LocalDateTime start,
                                               LocalDateTime end,
                                               Collection<String> uris,
                                               boolean isUniqueIps) throws StartAfterEndException {
        log.info("Get statistics from start {} to end {} with uris = {}, unique = {}", start, end, uris, isUniqueIps);
        StatsValidation.validateStartAndEnd(start, end);
        if (isUniqueIps) {
            return repository.viewStatsForUrisUniqueIps(uris, start, end);
        }
        if (CollectionUtils.isEmpty(uris)) {
            return repository.viewStats(start, end);
        }
        return repository.viewStatsForUris(uris, start, end);
    }
}
