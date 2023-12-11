package ru.practicum.mapper;

import ru.practicum.dto.categories.CategoryDto;
import ru.practicum.dto.events.*;
import ru.practicum.dto.users.UserShortDto;
import ru.practicum.model.*;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.stream.Collectors;

public class EventMapper {
    private static final int PARTICIPATION_DEFAULT_LIMIT = 0;

    public static EventEntity toEventEntity(NewEventDto dto) {
        return EventEntity.builder()
                .annotation(dto.getAnnotation())
                .category(makeCategoryEntity(dto))
                .createdOn(LocalDateTime.now())
                .description(dto.getDescription())
                .eventDate(dto.getEventDate())
                .lat(dto.getLocation().getLat())
                .lon(dto.getLocation().getLon())
                .paid(getPaid(dto))
                .participantLimit(getParticipantLimit(dto))
                .requestModeration(getRequestModeration(dto))
                .state(EventState.PENDING)
                .title(dto.getTitle())
                .build();
    }

    public static EventEntity toEventEntity(EventEntity entity, UpdateEventUserRequest dto) {
        return EventEntity.builder()
                .id(entity.getId())
                .annotation(getAnnotation(entity, dto))
                .createdOn(entity.getCreatedOn())
                .description(getDescription(entity, dto))
                .eventDate(getEventDate(entity, dto))
                .lat(getLat(entity, dto))
                .lon(getLon(entity, dto))
                .paid(getPaid(entity, dto))
                .participantLimit(getParticipantLimit(entity, dto))
                .requestModeration(getRequestModeration(entity, dto))
                .state(getState(entity, dto))
                .title(getTitle(entity, dto))
                .initiator(entity.getInitiator())
                .build();
    }

    public static EventEntity toEventEntity(EventEntity entity, UpdateEventAdminRequest dto) {
        return EventEntity.builder()
                .id(entity.getId())
                .annotation(getAnnotation(entity, dto))
                .createdOn(entity.getCreatedOn())
                .description(getDescription(entity, dto))
                .eventDate(getEventDate(entity, dto))
                .lat(getLat(entity, dto))
                .lon(getLon(entity, dto))
                .paid(getPaid(entity, dto))
                .participantLimit(getParticipantLimit(entity, dto))
                .requestModeration(getRequestModeration(entity, dto))
                .state(getState(entity, dto))
                .title(getTitle(entity, dto))
                .initiator(entity.getInitiator())
                .category(entity.getCategory())
                .publishedOn(LocalDateTime.now())
                .build();
    }

    public static EventFullDto toEventFullDto(EventEntity entity) {
        return EventFullDto.builder()
                .id(entity.getId())
                .annotation(entity.getAnnotation())
                .category(makeCategoryDto(entity))
                .createdOn(entity.getCreatedOn())
                .description(entity.getDescription())
                .eventDate(entity.getEventDate())
                .initiator(makeInitiator(entity))
                .location(makeLocation(entity))
                .paid(entity.getPaid())
                .participantLimit(entity.getParticipantLimit())
                .publishedOn(entity.getPublishedOn())
                .requestModeration(entity.getRequestModeration())
                .state(entity.getState())
                .title(entity.getTitle())
                .views(0L)
                .build();
    }

    public static EventShortDto toEventShortDto(EventEntity entity) {
        return EventShortDto.builder()
                .id(entity.getId())
                .annotation(entity.getAnnotation())
                .category(makeCategoryDto(entity))
                .eventDate(entity.getEventDate())
                .initiator(makeInitiator(entity))
                .paid(entity.getPaid())
                .title(entity.getTitle())
                .views(0L)
                .build();
    }


    public static Collection<EventShortDto> toEventShortDtoList(Collection<EventEntity> entities) {
        return entities.stream()
                .map(EventMapper::toEventShortDto)
                .collect(Collectors.toList());
    }

    private static CategoryEntity makeCategoryEntity(NewEventDto dto) {
        return CategoryEntity.builder()
                .id(dto.getCategory())
                .build();
    }

    private static CategoryDto makeCategoryDto(EventEntity entity) {
        return CategoryMapper.toCategoryDto(entity.getCategory());
    }

    private static UserShortDto makeInitiator(EventEntity entity) {
        return UserMapper.toUserShortDto(entity.getInitiator());
    }

    private static Location makeLocation(EventEntity entity) {
        return Location.builder()
                .lat(entity.getLat())
                .lon(entity.getLon())
                .build();
    }

    private static boolean getPaid(NewEventDto dto) {
        return dto.getPaid() != null && dto.getPaid();
    }

    private static boolean getPaid(EventEntity entity, UpdateEventUserRequest dto) {
        return dto.getPaid() == null ? entity.getPaid() : dto.getPaid();
    }

    private static boolean getPaid(EventEntity entity, UpdateEventAdminRequest dto) {
        return dto.getPaid() == null ? entity.getPaid() : dto.getPaid();
    }

    private static long getParticipantLimit(NewEventDto dto) {
        return dto.getParticipantLimit() == null ? PARTICIPATION_DEFAULT_LIMIT : dto.getParticipantLimit();
    }

    private static long getParticipantLimit(EventEntity entity, UpdateEventUserRequest dto) {
        return dto.getParticipantLimit() == null ? entity.getParticipantLimit() : dto.getParticipantLimit();
    }

    private static long getParticipantLimit(EventEntity entity, UpdateEventAdminRequest dto) {
        return dto.getParticipantLimit() == null ? entity.getParticipantLimit() : dto.getParticipantLimit();
    }

    private static boolean getRequestModeration(NewEventDto dto) {
        return dto.getRequestModeration() == null || dto.getRequestModeration();
    }

    private static boolean getRequestModeration(EventEntity entity, UpdateEventUserRequest dto) {
        return dto.getRequestModeration() == null ? entity.getRequestModeration() : dto.getRequestModeration();
    }

    private static boolean getRequestModeration(EventEntity entity, UpdateEventAdminRequest dto) {
        return dto.getRequestModeration() == null ? entity.getRequestModeration() : dto.getRequestModeration();
    }

    private static String getAnnotation(EventEntity entity, UpdateEventUserRequest dto) {
        return dto.getAnnotation() == null ? entity.getAnnotation() : dto.getAnnotation();
    }

    private static String getAnnotation(EventEntity entity, UpdateEventAdminRequest dto) {
        return dto.getAnnotation() == null ? entity.getAnnotation() : dto.getAnnotation();
    }

    private static String getDescription(EventEntity entity, UpdateEventUserRequest dto) {
        return dto.getDescription() == null ? entity.getDescription() : dto.getDescription();
    }

    private static String getDescription(EventEntity entity, UpdateEventAdminRequest dto) {
        return dto.getDescription() == null ? entity.getDescription() : dto.getDescription();
    }

    private static LocalDateTime getEventDate(EventEntity entity, UpdateEventUserRequest dto) {
        return dto.getEventDate() == null ? entity.getEventDate() : dto.getEventDate();
    }

    private static LocalDateTime getEventDate(EventEntity entity, UpdateEventAdminRequest dto) {
        return dto.getEventDate() == null ? entity.getEventDate() : dto.getEventDate();
    }

    private static float getLat(EventEntity entity, UpdateEventUserRequest dto) {
        return dto.getLocation() == null ? entity.getLat() : dto.getLocation().getLat();
    }

    private static float getLat(EventEntity entity, UpdateEventAdminRequest dto) {
        return dto.getLocation() == null ? entity.getLat() : dto.getLocation().getLat();
    }

    private static float getLon(EventEntity entity, UpdateEventUserRequest dto) {
        return dto.getLocation() == null ? entity.getLon() : dto.getLocation().getLon();
    }

    private static float getLon(EventEntity entity, UpdateEventAdminRequest dto) {
        return dto.getLocation() == null ? entity.getLon() : dto.getLocation().getLon();
    }

    private static String getTitle(EventEntity entity, UpdateEventUserRequest dto) {
        return dto.getTitle() == null ? entity.getTitle() : dto.getTitle();
    }

    private static String getTitle(EventEntity entity, UpdateEventAdminRequest dto) {
        return dto.getTitle() == null ? entity.getTitle() : dto.getTitle();
    }

    private static EventState getState(EventEntity entity, UpdateEventUserRequest dto) {
        if (dto.getStateAction() == null) {
            return entity.getState();
        }
        if (dto.getStateAction().equals(EventStateUserAction.SEND_TO_REVIEW)) {
            return EventState.PENDING;
        }
        return EventState.CANCELED;
    }

    private static EventState getState(EventEntity entity, UpdateEventAdminRequest dto) {
        if (dto.getStateAction() == null) {
            return entity.getState();
        }
        if (dto.getStateAction().equals(EventStateAdminAction.PUBLISH_EVENT)) {
            return EventState.PUBLISHED;
        }
        return EventState.CANCELED;
    }
}
