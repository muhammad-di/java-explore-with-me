package ru.practicum.service.requests;

import ru.practicum.dto.requests.EventRequestStatusUpdateRequest;
import ru.practicum.dto.requests.EventRequestStatusUpdateResult;
import ru.practicum.exception.*;
import ru.practicum.model.RequestEntity;

import java.util.Collection;

public interface RequestService {
    RequestEntity create(Long userId, Long eventId)
            throws UserNotFoundException,
            EventNotFoundException,
            EventIsNotPublishedException,
            RequestAlreadyExistsException,
            ParticipationLimitReachedException,
            RequesterAndInitiatorAreSameException;

    Collection<RequestEntity> findAll(Long userId) throws UserNotFoundException;

    RequestEntity cancel(Long userId, Long requestId) throws UserNotFoundException,
            RequestNotFoundException,
            RequesterAndClaimedRequesterAreNotSameException;

    Collection<RequestEntity> findRequestsForEvent(Long initiatorId, Long eventId)
            throws UserNotFoundException,
            EventNotFoundException,
            IncorrectInitiatorException;

    EventRequestStatusUpdateResult updateRequestsStatus(Long initiatorId, Long eventId, EventRequestStatusUpdateRequest requestBody) throws UserNotFoundException, EventNotFoundException, IncorrectInitiatorException, RequestIsNotPendingException, ParticipationLimitReachedException;
}
