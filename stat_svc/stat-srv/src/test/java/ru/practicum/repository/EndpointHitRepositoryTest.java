package ru.practicum.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.model.EndpointHitEntity;
import ru.practicum.model.ViewStats;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.samePropertyValuesAs;

@Slf4j
@DataJpaTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@AutoConfigureTestDatabase
public class EndpointHitRepositoryTest {

    @Autowired
    private final EntityManager em;

    @Autowired
    private final EndpointHitRepository repository;

    private EndpointHitEntity ep1;
    private EndpointHitEntity ep2;
    private EndpointHitEntity ep3;
    private EndpointHitEntity ep4;
    private EndpointHitEntity ep5;
    private EndpointHitEntity ep6;
    private EndpointHitEntity ep7;
    private EndpointHitEntity ep8;
    private EndpointHitEntity ep9;
    private EndpointHitEntity ep10;
    private EndpointHitEntity ep11;
    private EndpointHitEntity ep12;
    private EndpointHitEntity ep13;
    private EndpointHitEntity ep14;
    private EndpointHitEntity ep15;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final Collection<String> uris = List.of("/events/1", "/events/2", "/events/3");
    private static final LocalDateTime start = LocalDateTime.parse("2014-04-02 00:00:00", formatter);
    private static final LocalDateTime end = LocalDateTime.parse("2014-05-04 00:00:00", formatter);


    @BeforeEach
    public void createUsers() {

//      "/events/1"
//      192.163.0.1
        ep1 = EndpointHitEntity.builder().app("ewm-main-service")
                .uri("/events/1")
                .ip("192.163.0.1")
                .hitTime(LocalDateTime.parse("2014-04-04 20:00:00", formatter))
                .build();
        ep2 = EndpointHitEntity.builder().app("ewm-main-service")
                .uri("/events/1")
                .ip("192.163.0.1")
                .hitTime(LocalDateTime.parse("2014-04-05 20:00:00", formatter))
                .build();
        ep3 = EndpointHitEntity.builder().app("ewm-main-service")
                .uri("/events/1")
                .ip("192.163.0.1")
                .hitTime(LocalDateTime.parse("2014-04-06 20:00:00", formatter))
                .build();
        ep4 = EndpointHitEntity.builder().app("ewm-main-service")
                .uri("/events/1")
                .ip("192.163.0.1")
                .hitTime(LocalDateTime.parse("2014-04-07 20:00:00", formatter))
                .build();
        //      192.163.0.5
        ep5 = EndpointHitEntity.builder().app("ewm-main-service")
                .uri("/events/1")
                .ip("192.163.0.5")
                .hitTime(LocalDateTime.parse("2014-04-04 20:00:00", formatter))
                .build();
        ep6 = EndpointHitEntity.builder().app("ewm-main-service")
                .uri("/events/1")
                .ip("192.163.0.5")
                .hitTime(LocalDateTime.parse("2014-04-05 20:00:00", formatter))
                .build();
        ep7 = EndpointHitEntity.builder().app("ewm-main-service")
                .uri("/events/1")
                .ip("192.163.0.5")
                .hitTime(LocalDateTime.parse("2014-04-06 20:00:00", formatter))
                .build();
        ep8 = EndpointHitEntity.builder().app("ewm-main-service")
                .uri("/events/1")
                .ip("192.163.0.5")
                .hitTime(LocalDateTime.parse("2014-04-07 20:00:00", formatter))
                .build();

//      "/events/2"
//      192.163.0.1
        ep9 = EndpointHitEntity.builder().app("ewm-main-service")
                .uri("/events/2")
                .ip("192.163.0.1")
                .hitTime(LocalDateTime.parse("2014-04-04 20:00:00", formatter))
                .build();
        ep10 = EndpointHitEntity.builder().app("ewm-main-service")
                .uri("/events/2")
                .ip("192.163.0.1")
                .hitTime(LocalDateTime.parse("2014-04-05 20:00:00", formatter))
                .build();
        ep11 = EndpointHitEntity.builder().app("ewm-main-service")
                .uri("/events/2")
                .ip("192.163.0.1")
                .hitTime(LocalDateTime.parse("2014-04-06 20:00:00", formatter))
                .build();
        ep12 = EndpointHitEntity.builder().app("ewm-main-service")
                .uri("/events/2")
                .ip("192.163.0.1")
                .hitTime(LocalDateTime.parse("2014-04-07 20:00:00", formatter))
                .build();

        //      "/events/3"
        //      192.163.0.1
        ep13 = EndpointHitEntity.builder().app("ewm-main-service")
                .uri("/events/3")
                .ip("192.163.0.1")
                .hitTime(LocalDateTime.parse("2014-04-04 20:00:00", formatter))
                .build();
        ep14 = EndpointHitEntity.builder().app("ewm-main-service")
                .uri("/events/3")
                .ip("192.163.0.1")
                .hitTime(LocalDateTime.parse("2014-04-05 20:00:00", formatter))
                .build();
        ep15 = EndpointHitEntity.builder().app("ewm-main-service")
                .uri("/events/3")
                .ip("192.163.0.1")
                .hitTime(LocalDateTime.parse("2014-04-06 20:00:00", formatter))
                .build();
    }

    private void insertAllEndpointHits() {
        repository.save(ep1);
        repository.save(ep2);
        repository.save(ep3);
        repository.save(ep4);
        repository.save(ep5);
        repository.save(ep6);
        repository.save(ep7);
        repository.save(ep8);
        repository.save(ep9);
        repository.save(ep10);
        repository.save(ep11);
        repository.save(ep12);
        repository.save(ep13);
        repository.save(ep14);
        repository.save(ep15);
    }


    @Test
    public void shouldSaveEndpointHitEntity() {
        EndpointHitEntity ep = repository.save(ep1);

        TypedQuery<EndpointHitEntity> queryForEndpointHitSave = em.createQuery(
                "Select eh from EndpointHitEntity eh where eh.id = :id",
                EndpointHitEntity.class);
        EndpointHitEntity epFromQuery = queryForEndpointHitSave.setParameter("id", ep.getId()).getSingleResult();

        assertThat(ep, samePropertyValuesAs(epFromQuery));


        List<EndpointHitEntity> endpointHits = repository.findAll();
        log.info("1------------{}", endpointHits);
    }

    @Test
    public void shouldFindEndpointHitEntityById() {
        EndpointHitEntity ep = repository.save(ep1);
        EndpointHitEntity epFoundById = repository.findById(ep.getId()).orElseThrow(() -> new RuntimeException("END_POINT_HIT WAS NOT FOUND"));

        TypedQuery<EndpointHitEntity> queryForEndpointHitSave = em.createQuery(
                "Select eh from EndpointHitEntity eh where eh.id = :id",
                EndpointHitEntity.class);
        EndpointHitEntity epFromQuery = queryForEndpointHitSave.setParameter("id", ep.getId()).getSingleResult();

        assertThat(epFoundById, samePropertyValuesAs(epFromQuery));


        List<EndpointHitEntity> endpointHits = repository.findAll();
        log.info("2------------{}", endpointHits);
    }

    @Test
    public void shouldFindAllByUriInAndHitTimeBetween() {
        insertAllEndpointHits();
        Collection<String> uris = List.of("/events/1", "/events/2", "/events/3");
        List<ViewStats> list = repository.viewStatsForUris(uris, start, end);

//        TypedQuery<EndpointHitEntity> emQuery = em.createQuery(
//                "select distinct \n" +
//                        "eh.uri,\n" +
//                        "eh.ip\n" +
//                        "from EndpointHitEntity eh\n" +
//                        "where \n" +
//                        "eh.hitTime between :start and :end\n" +
//                        "and\n" +
//                        "eh.uri in (:uris)",
//                EndpointHitEntity.class);
//        List<EndpointHitEntity> epListFromQuery = emQuery
//                .setParameter("start", start)
//                .setParameter("end", end)
//                .setParameter("uris", uris)
//                .getResultList();

//        assertThat(list, samePropertyValuesAs(epListFromQuery));

        List<EndpointHitEntity> endpointHits = repository.findAll();
        log.info("3--------------------------------------------------------------------------------------------------");
        log.info("---------------------------------------------------------------------------------------------------");
        log.info("---------------------------------------------------------------------------------------------------");
        log.info("---------------------------------------------------------------------------------------------------");
        log.info("endpointHits------------{}", endpointHits);
        log.info("list------------{}", list);
        log.info("END------------------------------------------------------------------------------------------------");
        log.info("---------------------------------------------------------------------------------------------------");
        log.info("---------------------------------------------------------------------------------------------------");
        log.info("---------------------------------------------------------------------------------------------------");

    }


    @Test
    public void shouldFindAllByUriInAndHitTimeBetween2() {
        insertAllEndpointHits();
        Collection<String> uris = List.of("/events/1", "/events/2", "/events/3");
//        List<ViewStats> list = repository.viewStatsForUrisUniqueIps(uris, start, end);




        List<EndpointHitEntity> endpointHits = repository.findAll();
        log.info("3--------------------------------------------------------------------------------------------------");
        log.info("---------------------------------------------------------------------------------------------------");
        log.info("---------------------------------------------------------------------------------------------------");
        log.info("---------------------------------------------------------------------------------------------------");
        log.info("endpointHits------------{}", endpointHits);
//        log.info("list------------{}", list);
        log.info("END------------------------------------------------------------------------------------------------");
        log.info("---------------------------------------------------------------------------------------------------");
        log.info("---------------------------------------------------------------------------------------------------");
        log.info("---------------------------------------------------------------------------------------------------");

    }
}
