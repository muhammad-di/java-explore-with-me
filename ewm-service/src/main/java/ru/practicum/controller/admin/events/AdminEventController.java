package ru.practicum.controller.admin.events;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.events.EventFullDto;
import ru.practicum.dto.events.UpdateEventAdminRequest;
import ru.practicum.exception.*;
import ru.practicum.mapper.EventMapper;
import ru.practicum.model.EventEntity;
import ru.practicum.model.EventState;
import ru.practicum.service.events.EventService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.Collection;


@RestController
@RequestMapping(path = "/admin/events")
@RequiredArgsConstructor
@Validated
public class AdminEventController {
    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private final EventService service;


    @GetMapping
    public Collection<EventFullDto> findAll(@RequestParam(name = "users", required = false) Collection<Long> users,
                                            @RequestParam(name = "states", required = false) Collection<EventState> states,
                                            @RequestParam(name = "categories", required = false) Collection<Long> categories,
                                            @DateTimeFormat(pattern = DATE_TIME_FORMAT)
                                            @RequestParam(name = "rangeStart", required = false) LocalDateTime rangeStart,
                                            @DateTimeFormat(pattern = DATE_TIME_FORMAT)
                                            @RequestParam(name = "rangeEnd", required = false) LocalDateTime rangeEnd,
                                            @PositiveOrZero
                                            @RequestParam(name = "from", defaultValue = "0") Integer from,
                                            @Positive
                                            @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return service.findAll(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping(path = "/{eventId}")
    public EventFullDto update(@Positive @PathVariable(name = "eventId") Long eventId,
                               @Valid @RequestBody UpdateEventAdminRequest dto)
            throws UserNotFoundException,
            EventNotFoundException,
            EventDateInPastException,
            CategoryNotFoundException,
            EventIsAlreadyCanceledException,
            EventIsAlreadyPublishedException,
            EventDateBeforeOneHourFromPublishingException {
        EventEntity entity = service.update(eventId, dto);

        return EventMapper.toEventFullDto(entity);
    }

}
