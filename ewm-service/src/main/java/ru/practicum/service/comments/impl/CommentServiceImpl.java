package ru.practicum.service.comments.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import ru.practicum.client.StatsClient;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.dto.comments.CommentDto;
import ru.practicum.dto.events.EventShortDto;
import ru.practicum.exception.CommenterAndInitiatorAreSameException;
import ru.practicum.exception.EventIsNotPublishedException;
import ru.practicum.exception.EventNotFoundException;
import ru.practicum.exception.UserNotFoundException;
import ru.practicum.mapper.CommentMapper;
import ru.practicum.mapper.EventMapper;
import ru.practicum.mapper.ViewStatsMapper;
import ru.practicum.model.CommentEntity;
import ru.practicum.model.EventEntity;
import ru.practicum.model.RequestStatus;
import ru.practicum.model.UserEntity;
import ru.practicum.repository.comments.CommentRepository;
import ru.practicum.repository.events.EventRepository;
import ru.practicum.repository.requests.RequestRepository;
import ru.practicum.repository.users.UserRepository;
import ru.practicum.service.comments.CommentsService;
import ru.practicum.utils.CommentsUtils;
import ru.practicum.utils.EventsUtils;
import ru.practicum.validation.CommentValidation;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentsService {
    private final StatsClient statsClient;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final RequestRepository requestRepository;
    private final CommentRepository commentRepository;


    @Override
    public CommentDto create(Long commenterId, Long eventId, CommentEntity commentEntity)
            throws CommenterAndInitiatorAreSameException, EventIsNotPublishedException, UserNotFoundException, EventNotFoundException {
        EventEntity eventEntity;
        UserEntity commenterEntity;
        EventShortDto eventShortDto;
        CommentEntity commentEntityFromDB;

        commenterEntity = findUserById(commenterId);
        eventEntity = findEventById(eventId);
        CommentValidation.validate(commenterEntity, eventEntity);
        commentEntity.setCreated(CommentsUtils.getDefaultCommentCreatedDate());
        commentEntity.setCommenter(commenterEntity);
        commentEntity.setEvent(eventEntity);
        commentEntityFromDB = commentRepository.save(commentEntity);
        eventShortDto = getEventShortDto(eventEntity);

        return CommentMapper.toCommentDto(commentEntityFromDB, eventShortDto);
    }

    //Common methods
    private UserEntity findUserById(Long userId) throws UserNotFoundException {
        return userRepository.findById(userId)
                .orElseThrow(() -> {
                    String message = String.format("a user with id { %d } was not found", userId);
                    return new UserNotFoundException(message);
                });
    }

    private EventEntity findEventById(Long eventId) throws EventNotFoundException {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> {
                    String message = String.format("an event with id { %d } was not found", eventId);
                    return new EventNotFoundException(message);
                });
    }


    // EventShortDto
    private EventShortDto getEventShortDto(EventEntity eventEntity) {
        EventShortDto eventShortDto;

        eventShortDto = EventMapper.toEventShortDto(eventEntity);
        setConfirmedRequests(eventShortDto);
        setViewsForEvent(eventShortDto);

        return eventShortDto;
    }

    private void setConfirmedRequests(EventShortDto eventShortDto) {
        long eventId;
        long confirmedRequests;

        eventId = eventShortDto.getId();
        confirmedRequests = countConfirmedRequestsForEvent(eventId);
        eventShortDto.setConfirmedRequests(confirmedRequests);
    }

    private void setViewsForEvent(EventShortDto eventShortDto) {
        long eventId;
        long views;

        eventId = eventShortDto.getId();
        views = countViewsForEvent(eventId);
        eventShortDto.setViews(views);
    }

    private long countConfirmedRequestsForEvent(long eventId) {
        RequestStatus status;

        status = RequestStatus.CONFIRMED;
        return requestRepository.countByEventIdAndStatus(eventId, status);
    }

    private long countViewsForEvent(long eventId) {
        String path;
        ViewStatsDto viewStatsDto;
        ResponseEntity<Object> response;
        Collection<ViewStatsDto> viewStatsDtoList;
        Collection<LinkedHashMap<String, Object>> body;
        LocalDateTime end = EventsUtils.getDefaultEndForEndpointHit();
        LocalDateTime start = EventsUtils.getDefaultStartForEndpointHit();

        path = "/events/".concat(String.valueOf(eventId));
        response = statsClient.get(start, end, List.of(path), true);
        body = (Collection<LinkedHashMap<String, Object>>) response.getBody();
        if (!ObjectUtils.isEmpty(body)) {
            viewStatsDtoList = ViewStatsMapper.toViewStatsDtoList(body);
            if (!ObjectUtils.isEmpty(viewStatsDtoList)) {
                viewStatsDto = viewStatsDtoList.stream()
                        .findFirst()
                        .orElse(EventsUtils.getDefaultViewStatsDto());

                return viewStatsDto.getHits();
            }
        }

        return EventsUtils.getDefaultEndpointHits();
    }
}
