package ru.practicum.repository.requests;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.RequestEntity;
import ru.practicum.model.RequestStatus;

import java.util.Collection;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<RequestEntity, Long> {

    Collection<RequestEntity> findAllByIdIn(Collection<Long> requestIds);

    Optional<RequestEntity> findByRequesterIdAndEventId(Long requesterId, Long eventId);

    Collection<RequestEntity> findAllByEventIdAndStatus(Long eventId, RequestStatus status);

    Collection<RequestEntity> findAllByRequesterId(Long requesterId);

    Collection<RequestEntity> findAllByEventId(Long eventId);

    long countByEventIdAndStatus(Long eventId, RequestStatus status);
}
