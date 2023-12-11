package ru.practicum.controller.events;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.events.EventFullDto;
import ru.practicum.dto.events.NewEventDto;
import ru.practicum.dto.events.UpdateEventUserRequest;
import ru.practicum.exception.*;
import ru.practicum.mapper.EventMapper;
import ru.practicum.model.EventEntity;
import ru.practicum.service.events.EventService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Validated
public class UserEventController {
    private final EventService service;


    @PostMapping(path = "/{userId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto create(@Positive @PathVariable(name = "userId") Long userId,
                               @Valid @RequestBody NewEventDto dto)
            throws UserNotFoundException,
            CategoryNotFoundException,
            EventDateInPastException,
            EventDateBeforeTwoHoursException {
        EventEntity entity = EventMapper.toEventEntity(dto);

        return EventMapper.toEventFullDto(service.create(userId, entity));
    }

    @GetMapping(path = "/{userId}/events")
    public Collection<EventFullDto> findAll(@Positive @PathVariable(name = "userId") Long userId,
                                            @PositiveOrZero
                                            @RequestParam(name = "from", defaultValue = "0") Integer from,
                                            @Positive
                                            @RequestParam(name = "size", defaultValue = "10") Integer size)
            throws UserNotFoundException {
        Collection<EventEntity> list = service.findAll(userId, from, size);

        return list.stream()
                .map(EventMapper::toEventFullDto)
                .collect(Collectors.toList());
    }

    @GetMapping(path = "/{userId}/events/{eventId}")
    public EventFullDto findById(@Positive @PathVariable(name = "userId") Long userId,
                                 @Positive @PathVariable(name = "eventId") Long eventId)
            throws UserNotFoundException, IncorrectInitiatorException, EventNotFoundException {

        return EventMapper.toEventFullDto(service.findById(userId, eventId));
    }

    @PatchMapping(path = "/{userId}/events/{eventId}")
    public EventFullDto update(@Positive @PathVariable(name = "userId") Long userId,
                               @Positive @PathVariable(name = "eventId") Long eventId,
                               @Valid @RequestBody UpdateEventUserRequest dto)
            throws UserNotFoundException,
            EventNotFoundException,
            EventDateInPastException,
            CategoryNotFoundException,
            IncorrectInitiatorException,
            EventDateBeforeTwoHoursException,
            EventIsAlreadyPublishedException {
        EventEntity entity = service.update(userId, eventId, dto);

        return EventMapper.toEventFullDto(entity);
    }
}
