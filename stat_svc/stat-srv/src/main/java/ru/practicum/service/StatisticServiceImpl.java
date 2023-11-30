package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.model.EndpointHitEntity;
import ru.practicum.repository.EndpointHitRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticServiceImpl implements StatisticService {
    private final EndpointHitRepository repository;

    @Transactional
    @Override
    public void save(EndpointHitEntity endpointHitEntity) {
        repository.save(endpointHitEntity);
    }

    @Transactional(readOnly = true)
    @Override
    public List<EndpointHitEntity> findAllBetweenDates(LocalDateTime start,
                                                       LocalDateTime end,
                                                       Collection<String> uris,
                                                       boolean isUniqueIps) {
//        if (isUniqueIps) {
//            return repository.findAllUniqueIp(uris, start, end);
//        }
//        return repository.findAllByUriInAndHitTimeBetween(uris, start, end);
        return null;
    }
}
