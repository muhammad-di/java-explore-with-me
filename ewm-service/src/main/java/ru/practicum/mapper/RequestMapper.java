package ru.practicum.mapper;

import ru.practicum.dto.requests.EventRequestStatusUpdateResult;
import ru.practicum.dto.requests.ParticipationRequestDto;
import ru.practicum.model.EventEntity;
import ru.practicum.model.RequestEntity;
import ru.practicum.model.RequestStatus;
import ru.practicum.model.UserEntity;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

public class RequestMapper {
    private static final LocalDateTime REQUEST_CREATED_TIME = LocalDateTime.now();
    private static final RequestStatus DEFAULT_REQUEST_STATUS = RequestStatus.PENDING;


    public static ParticipationRequestDto toParticipationRequestDto(RequestEntity entity) {
        return ParticipationRequestDto.builder()
                .id(entity.getId())
                .created(entity.getCreated())
                .event(entity.getEvent().getId())
                .requester(entity.getRequester().getId())
                .status(entity.getStatus())
                .build();
    }

    public static RequestEntity toRequestEntity(UserEntity requesterEntity, EventEntity eventEntity) {
        return RequestEntity.builder()
                .requester(requesterEntity)
                .event(eventEntity)
                .created(REQUEST_CREATED_TIME)
                .status(DEFAULT_REQUEST_STATUS)
                .build();
    }


    public static EventRequestStatusUpdateResult toEventRequestStatusUpdateResult(
            Collection<ParticipationRequestDto> confirmedRequests,
            Collection<ParticipationRequestDto> rejectedRequests) {
        return EventRequestStatusUpdateResult.builder()
                .confirmedRequests(confirmedRequests == null ? Collections.emptyList() : confirmedRequests)
                .rejectedRequests(rejectedRequests == null ? Collections.emptyList() : rejectedRequests)
                .build();
    }

    public static Collection<ParticipationRequestDto> toParticipationRequestDtoList(Collection<RequestEntity> entityList) {
        return entityList.stream()
                .map(RequestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }
}