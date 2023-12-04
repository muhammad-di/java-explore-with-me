package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.EndPointHitDto;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.exception.StartAfterEndException;
import ru.practicum.mapper.EndPointHitMapper;
import ru.practicum.mapper.ViewStatsMapper;
import ru.practicum.model.EndpointHitEntity;
import ru.practicum.model.ViewStats;
import ru.practicum.service.StatsService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping
@RequiredArgsConstructor
@Validated
public class StatsController {
    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private final StatsService service;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerEndpointHit(@RequestBody @Valid EndPointHitDto endPointHitDto) {
        EndpointHitEntity entity = EndPointHitMapper.toEndPointHitEntity(endPointHitDto);
        service.save(entity);
    }

    @GetMapping("/stats")
    public List<ViewStatsDto> viewEndPointHit(@DateTimeFormat(pattern = DATE_TIME_FORMAT)
                                              @RequestParam(name = "start", required = true) LocalDateTime start,
                                              @DateTimeFormat(pattern = DATE_TIME_FORMAT)
                                              @RequestParam(name = "end", required = true) LocalDateTime end,
                                              @RequestParam(name = "uris", required = false) Collection<String> uris,
                                              @RequestParam(name = "unique", defaultValue = "false") boolean unique)
            throws StartAfterEndException {
        List<ViewStats> list = service.findAllBetweenDates(start, end, uris, unique);
        return list.stream()
                .map(ViewStatsMapper::toViewStatsDto)
                .collect(Collectors.toList());
    }
}