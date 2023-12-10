package ru.practicum.service.events;

import ru.practicum.dto.events.EventFullDto;
import ru.practicum.dto.events.EventShortDto;
import ru.practicum.dto.events.UpdateEventAdminRequest;
import ru.practicum.dto.events.UpdateEventUserRequest;
import ru.practicum.exception.*;
import ru.practicum.model.EventEntity;
import ru.practicum.model.EventSort;
import ru.practicum.model.EventState;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Collection;

public interface EventService {
    EventEntity create(Long userId, EventEntity entity) throws UserNotFoundException,
            CategoryNotFoundException,
            EventDateInPastException,
            EventDateBeforeTwoHoursException;

    EventEntity findById(Long userId, Long eventId) throws UserNotFoundException,
            EventNotFoundException,
            IncorrectInitiatorException;

    EventFullDto findById(Long eventId, HttpServletRequest request) throws UserNotFoundException,
            EventNotFoundException,
            IncorrectInitiatorException, EventIsNotPublishedForPublicException;

    EventEntity update(Long userId, Long eventId, UpdateEventUserRequest dto) throws UserNotFoundException,
            EventNotFoundException,
            IncorrectInitiatorException,
            CategoryNotFoundException,
            EventDateInPastException,
            EventDateBeforeTwoHoursException, EventIsAlreadyPublishedException;

    EventEntity update(Long eventId, UpdateEventAdminRequest dto) throws UserNotFoundException,
            EventNotFoundException,
            CategoryNotFoundException,
            EventDateInPastException,
            EventDateBeforeOneHourFromPublishingException, EventIsAlreadyCanceledException, EventIsAlreadyPublishedException;

    Collection<EventEntity> findAll(Long userId, Integer from, Integer size) throws UserNotFoundException;

    Collection<EventFullDto> findAll(Collection<Long> users,
                                     Collection<EventState> states,
                                     Collection<Long> categories,
                                     LocalDateTime rangeStart,
                                     LocalDateTime rangeEnd,
                                     Integer from,
                                     Integer size);

    Collection<EventShortDto> search(String text,
                                     Collection<Long> categories,
                                     Boolean paid,
                                     Boolean onlyAvailable,
                                     EventSort sort,
                                     LocalDateTime rangeStart,
                                     LocalDateTime rangeEnd,
                                     Integer from,
                                     Integer size,
                                     HttpServletRequest request) throws RangeStartIsAfterRangeEndException;
}
