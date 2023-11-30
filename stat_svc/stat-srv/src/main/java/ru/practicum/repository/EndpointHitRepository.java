package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.model.EndpointHitEntity;
import ru.practicum.model.ViewStats;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface EndpointHitRepository extends
        JpaRepository<EndpointHitEntity, Long>{

    @Query("select\n" +
            "new ru.practicum.model.ViewStats(eh.app, eh.uri, count(eh))\n" +
            "from EndpointHitEntity eh\n" +
            "where \n" +
            "eh.uri in (:uris)\n" +
            "and\n" +
            "eh.hitTime between :start and :end\n" +
            "group by eh.app, eh.uri"
    )
    List<ViewStats> viewStatsForUris(Collection<String> uris, LocalDateTime start, LocalDateTime end);

//
//    @Query(nativeQuery = true)
//    List<ViewStats> viewStatsForUrisUniqueIps(Collection<String> uris, LocalDateTime start, LocalDateTime end);

}
