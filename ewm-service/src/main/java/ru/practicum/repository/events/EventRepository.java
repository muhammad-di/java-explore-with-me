package ru.practicum.repository.events;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.model.EventEntity;
import ru.practicum.model.EventState;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface EventRepository extends JpaRepository<EventEntity, Long> {

    Collection<EventEntity> findAllByIdIn(Collection<Long> ids);

    Page<EventEntity> findAllByInitiatorId(Long userId, Pageable sortedByIdAsc);

    @Query("SELECT e from EventEntity e " +
            "WHERE " +
            "e.initiator.id IN(:users)")
    Page<EventEntity> findAllForAdmin(@Param("users") Collection<Long> users, Pageable sortedByIdAsc);

    @Query("SELECT e from EventEntity e " +
            "WHERE " +
            "e.initiator.id IN(:users) " +
            "AND " +
            "e.state IN(:states)")
    Page<EventEntity> findAllForAdmin(@Param("users") Collection<Long> users,
                                      @Param("states") Collection<EventState> states,
                                      Pageable sortedByIdAsc);

    @Query("SELECT e from EventEntity e " +
            "WHERE " +
            "e.initiator.id IN(:users) " +
            "AND " +
            "e.state IN(:states) " +
            "AND " +
            "e.category.id IN(:categories)")
    Page<EventEntity> findAllForAdmin(@Param("users") Collection<Long> users,
                                      @Param("states") Collection<EventState> states,
                                      @Param("categories") Collection<Long> categories,
                                      Pageable sortedByIdAsc);

    @Query("SELECT e from EventEntity e " +
            "WHERE " +
            "e.initiator.id IN(:users) " +
            "AND " +
            "e.state IN(:states) " +
            "AND " +
            "e.category.id IN(:categories) " +
            "AND " +
            "e.eventDate BETWEEN :rangeStart AND :rangeEnd")
    Page<EventEntity> findAllForAdmin(@Param("users") Collection<Long> users,
                                      @Param("states") Collection<EventState> states,
                                      @Param("categories") Collection<Long> categories,
                                      @Param("rangeStart") LocalDateTime rangeStart,
                                      @Param("rangeEnd") LocalDateTime rangeEnd,
                                      Pageable sortedByIdAsc);

    @Query("SELECT e from EventEntity e " +
            "WHERE " +
            "e.state = :state " +
            "AND " +
            "e.eventDate >= :rangeStart")
    List<EventEntity> search(@Param("state") EventState state,
                             @Param("rangeStart") LocalDateTime rangeStart,
                             Pageable sortedByEventDateAsc);

    @Query("SELECT e from EventEntity e " +
            "WHERE " +
            "e.state = :state " +
            "AND " +
            "e.category.id IN(:categories) " +
            "AND " +
            "e.eventDate >= :rangeStart")
    List<EventEntity> search(@Param("state") EventState state,
                             @Param("categories") Collection<Long> categories,
                             @Param("rangeStart") LocalDateTime rangeStart,
                             Pageable sortedByEventDateAsc);


    @Query("SELECT e from EventEntity e " +
            "WHERE " +
            "upper(e.annotation) like upper(concat('%', :text, '%')) " +
            "OR " +
            "upper(e.description) like upper(concat('%', :text, '%')) " +
            "AND " +
            "e.state = :state " +
            "AND " +
            "e.eventDate >= :rangeStart")
    List<EventEntity> search(@Param("text") String text,
                             @Param("state") EventState state,
                             @Param("rangeStart") LocalDateTime rangeStart,
                             Pageable sortedByEventDateAsc);

    @Query("SELECT e from EventEntity e " +
            "WHERE " +
            "upper(e.annotation) like upper(concat('%', :text, '%')) " +
            "OR " +
            "upper(e.description) like upper(concat('%', :text, '%')) " +
            "AND " +
            "e.state = :state " +
            "AND " +
            "e.category.id IN(:categories) " +
            "AND " +
            "e.eventDate >= :rangeStart")
    List<EventEntity> search(@Param("text") String text,
                             @Param("state") EventState state,
                             @Param("categories") Collection<Long> categories,
                             @Param("rangeStart") LocalDateTime rangeStart,
                             Pageable sortedByEventDateAsc);

    @Query("SELECT e from EventEntity e " +
            "WHERE " +
            "upper(e.annotation) like upper(concat('%', :text, '%')) " +
            "OR " +
            "upper(e.description) like upper(concat('%', :text, '%')) " +
            "AND " +
            "e.state = :state " +
            "AND " +
            "e.category.id IN(:categories) " +
            "AND " +
            "e.paid = :paid " +
            "AND " +
            "e.eventDate >= :rangeStart")
    List<EventEntity> search(@Param("text") String text,
                             @Param("state") EventState state,
                             @Param("categories") Collection<Long> categories,
                             @Param("paid") Boolean paid,
                             @Param("rangeStart") LocalDateTime rangeStart,
                             Pageable sortedByEventDateAsc);

    @Query("SELECT e from EventEntity e " +
            "WHERE " +
            "upper(e.annotation) like upper(concat('%', :text, '%')) " +
            "OR " +
            "upper(e.description) like upper(concat('%', :text, '%')) " +
            "AND " +
            "e.state = :state " +
            "AND " +
            "e.category.id IN(:categories) " +
            "AND " +
            "e.paid = :paid " +
            "AND " +
            "e.eventDate BETWEEN :rangeStart AND :rangeEnd")
    List<EventEntity> search(@Param("text") String text,
                             @Param("state") EventState state,
                             @Param("categories") Collection<Long> categories,
                             @Param("paid") Boolean paid,
                             @Param("rangeStart") LocalDateTime rangeStart,
                             @Param("rangeEnd") LocalDateTime rangeEnd,
                             Pageable sortedByEventDateAsc);
}
