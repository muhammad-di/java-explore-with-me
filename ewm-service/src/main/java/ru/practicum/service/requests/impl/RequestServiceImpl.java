package ru.practicum.service.requests.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.requests.EventRequestStatusUpdateRequest;
import ru.practicum.dto.requests.EventRequestStatusUpdateResult;
import ru.practicum.dto.requests.ParticipationRequestDto;
import ru.practicum.exception.*;
import ru.practicum.mapper.RequestMapper;
import ru.practicum.model.EventEntity;
import ru.practicum.model.RequestEntity;
import ru.practicum.model.RequestStatus;
import ru.practicum.model.UserEntity;
import ru.practicum.repository.events.EventRepository;
import ru.practicum.repository.requests.RequestRepository;
import ru.practicum.repository.users.UserRepository;
import ru.practicum.service.requests.RequestService;
import ru.practicum.validation.EventValidation;
import ru.practicum.validation.RequestValidation;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private static final long PARTICIPATION_LIMIT_AT_WHICH_STATUS_CONFIRMED = 0;
    private final RequestRepository requestRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public RequestEntity create(Long userId, Long eventId)
            throws UserNotFoundException,
            EventNotFoundException,
            EventIsNotPublishedException,
            RequestAlreadyExistsException,
            ParticipationLimitReachedException,
            RequesterAndInitiatorAreSameException {
        UserEntity requesterEntity = findUserById(userId);
        EventEntity eventEntity = findEventById(eventId);
        long confirmedRequests;
        RequestEntity requestEntity;

        exists(userId, eventId);
        confirmedRequests = requestRepository.countByEventIdAndStatus(eventId, RequestStatus.CONFIRMED);
        RequestValidation.validate(requesterEntity, eventEntity, confirmedRequests);
        requestEntity = RequestMapper.toRequestEntity(requesterEntity, eventEntity);

        return create(eventEntity, requestEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<RequestEntity> findAll(Long userId) throws UserNotFoundException {
        findUserById(userId);
        return requestRepository.findAllByRequesterId(userId);
    }

    @Override
    @Transactional
    public RequestEntity cancel(Long userId, Long requestId)
            throws UserNotFoundException,
            RequestNotFoundException,
            RequesterAndClaimedRequesterAreNotSameException {
        UserEntity claimedRequesterEntity = findUserById(userId);
        RequestEntity requestEntity = findById(requestId);

        RequestValidation.validate(claimedRequesterEntity, requestEntity);
        requestEntity.setStatus(RequestStatus.CANCELED);

        return requestRepository.save(requestEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<RequestEntity> findRequestsForEvent(Long initiatorId, Long eventId)
            throws UserNotFoundException, EventNotFoundException, IncorrectInitiatorException {
        UserEntity initiatorEntity = findUserById(initiatorId);
        EventEntity eventEntity = findEventById(eventId);

        EventValidation.validateInitiator(initiatorEntity, eventEntity);

        return requestRepository.findAllByEventId(eventId);
    }

    @Override
    @Transactional
    public EventRequestStatusUpdateResult updateRequestsStatus(Long initiatorId,
                                                               Long eventId,
                                                               EventRequestStatusUpdateRequest requestBody)
            throws UserNotFoundException,
            EventNotFoundException,
            IncorrectInitiatorException,
            RequestIsNotPendingException,
            ParticipationLimitReachedException {
        UserEntity initiatorEntity;
        EventEntity eventEntity;
        long participationLimit;
        boolean requestModeration;
        Collection<RequestEntity> requests;

        initiatorEntity = findUserById(initiatorId);
        eventEntity = findEventById(eventId);
        participationLimit = eventEntity.getParticipantLimit();
        requestModeration = eventEntity.getRequestModeration();

        if (!requestModeration || participationLimit == PARTICIPATION_LIMIT_AT_WHICH_STATUS_CONFIRMED) {
            return findAllForEventWithoutModerationAndParticipationLimit(eventEntity);
        }

        EventValidation.validateInitiator(initiatorEntity, eventEntity);
        requests = findByIds(requestBody);
        updateRequests(requestBody, requests, eventEntity);

        return getEventRequestStatusUpdateResult(eventEntity);
    }

    private RequestEntity create(EventEntity eventEntity, RequestEntity requestEntity) {
        boolean requestModeration = eventEntity.getRequestModeration();
        long participationLimit = eventEntity.getParticipantLimit();
        RequestStatus status = RequestStatus.CONFIRMED;
        if (!requestModeration || participationLimit == PARTICIPATION_LIMIT_AT_WHICH_STATUS_CONFIRMED) {
            requestEntity.setStatus(status);
        }
        return requestRepository.save(requestEntity);
    }


    private RequestEntity findById(Long requestId) throws RequestNotFoundException {
        return requestRepository.findById(requestId)
                .orElseThrow(() -> {
                    String message = String.format("a request with id { %d } was not found", requestId);
                    return new RequestNotFoundException(message);
                });
    }

    private void exists(Long userId, Long eventId) throws RequestAlreadyExistsException {
        Optional<RequestEntity> optional = requestRepository.findByRequesterIdAndEventId(userId, eventId);
        String message = String.format("a participation request in event with id { %d }" +
                " made by user with id { %d } already exists", eventId, userId);

        if (optional.isPresent()) {
            throw new RequestAlreadyExistsException(message);
        }
    }

    private EventEntity findEventById(Long eventId) throws EventNotFoundException {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> {
                    String message = String.format("an event with id { %d } was not found", eventId);
                    return new EventNotFoundException(message);
                });
    }

    private UserEntity findUserById(Long userId) throws UserNotFoundException {
        return userRepository.findById(userId)
                .orElseThrow(() -> {
                    String message = String.format("a user with id { %d } was not found", userId);
                    return new UserNotFoundException(message);
                });
    }

    private Collection<RequestEntity> findByIds(EventRequestStatusUpdateRequest requestBody) {
        Collection<Long> requestIds = requestBody.getRequestIds();

        return requestRepository.findAllByIdIn(requestIds);
    }

    private void updateRequests(EventRequestStatusUpdateRequest requestBody,
                                Collection<RequestEntity> requests,
                                EventEntity eventEntity)
            throws RequestIsNotPendingException, ParticipationLimitReachedException {
        for (RequestEntity r : requests) {
            validateAndUpdateStatus(r, requestBody, eventEntity);
        }
    }


    private void validateAndUpdateStatus(RequestEntity request,
                                         EventRequestStatusUpdateRequest requestBody,
                                         EventEntity eventEntity)
            throws RequestIsNotPendingException, ParticipationLimitReachedException {
        long confirmedRequests = requestRepository.countByEventIdAndStatus(eventEntity.getId(), RequestStatus.CONFIRMED);
        try {
            RequestValidation.validate(request, eventEntity, confirmedRequests);
        } catch (ParticipationLimitReachedException e) {
            rejectRequestsByEvent(eventEntity);
            throw e;
        }

        updateStatus(request, requestBody);
    }

    private void updateStatus(RequestEntity request, EventRequestStatusUpdateRequest requestBody) {
        RequestStatus status = requestBody.getStatus();
        request.setStatus(status);

        requestRepository.save(request);
    }

    private void rejectRequestsByEvent(EventEntity eventEntity) {
        RequestStatus status = RequestStatus.PENDING;
        long eventId = eventEntity.getId();

        requestRepository.findAllByEventIdAndStatus(eventId, status)
                .forEach(this::rejectRequestByEvent);
    }


    private void rejectRequestByEvent(RequestEntity requestEntity) {
        RequestStatus status = RequestStatus.REJECTED;

        requestEntity.setStatus(status);
        requestRepository.save(requestEntity);
    }


    private EventRequestStatusUpdateResult findAllForEventWithoutModerationAndParticipationLimit(EventEntity eventEntity) {
        long eventId;
        Collection<RequestEntity> requestsList;
        Collection<ParticipationRequestDto> confirmedRequests;

        eventId = eventEntity.getId();
        requestsList = requestRepository.findAllByEventId(eventId);
        confirmedRequests = RequestMapper.toParticipationRequestDtoList(requestsList);

        return RequestMapper.toEventRequestStatusUpdateResult(confirmedRequests, null);
    }

    private EventRequestStatusUpdateResult getEventRequestStatusUpdateResult(EventEntity eventEntity) {
        long eventId;
        RequestStatus statusRejected;
        RequestStatus statusConfirmed;
        Collection<RequestEntity> rejectedRequestsEntities;
        Collection<RequestEntity> confirmedRequestsEntities;
        Collection<ParticipationRequestDto> rejectedRequestsDtoList;
        Collection<ParticipationRequestDto> confirmedRequestsDtoList;

        eventId = eventEntity.getId();
        statusRejected = RequestStatus.REJECTED;
        statusConfirmed = RequestStatus.CONFIRMED;
        rejectedRequestsEntities = requestRepository.findAllByEventIdAndStatus(eventId, statusRejected);
        confirmedRequestsEntities = requestRepository.findAllByEventIdAndStatus(eventId, statusConfirmed);
        rejectedRequestsDtoList = RequestMapper.toParticipationRequestDtoList(rejectedRequestsEntities);
        confirmedRequestsDtoList = RequestMapper.toParticipationRequestDtoList(confirmedRequestsEntities);

        return RequestMapper.toEventRequestStatusUpdateResult(confirmedRequestsDtoList, rejectedRequestsDtoList);
    }
}
