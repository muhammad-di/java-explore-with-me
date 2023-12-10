package ru.practicum.controller.events;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.events.EventFullDto;
import ru.practicum.dto.events.EventShortDto;
import ru.practicum.exception.*;
import ru.practicum.model.EventSort;
import ru.practicum.service.events.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.Collection;


@RestController
@RequestMapping(path = "/events")
@RequiredArgsConstructor
@Validated
public class EventController {
    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private final EventService service;


    @GetMapping
    public Collection<EventShortDto> findAll(@RequestParam(name = "text", required = false) String text,
                                             @RequestParam(name = "categories", required = false) Collection<Long> categories,
                                             @RequestParam(name = "paid", required = false) Boolean paid,
                                             @RequestParam(name = "onlyAvailable", required = false) Boolean onlyAvailable,
                                             @RequestParam(name = "sort", required = false) EventSort sort,
                                             @DateTimeFormat(pattern = DATE_TIME_FORMAT)
                                             @RequestParam(name = "rangeStart", required = false) LocalDateTime rangeStart,
                                             @DateTimeFormat(pattern = DATE_TIME_FORMAT)
                                             @RequestParam(name = "rangeEnd", required = false) LocalDateTime rangeEnd,
                                             @PositiveOrZero
                                             @RequestParam(name = "from", defaultValue = "0") Integer from,
                                             @Positive
                                             @RequestParam(name = "size", defaultValue = "10") Integer size,
                                             HttpServletRequest request)
            throws RangeStartIsAfterRangeEndException {
        return service.search(text, categories, paid, onlyAvailable, sort, rangeStart, rangeEnd, from, size, request);
    }

    @GetMapping(path = "/{id}")
    public EventFullDto findById(@Positive @PathVariable(name = "id") Long id, HttpServletRequest request)
            throws EventNotFoundException,
            UserNotFoundException,
            IncorrectInitiatorException,
            EventIsNotPublishedForPublicException {

        return service.findById(id, request);
    }
}
