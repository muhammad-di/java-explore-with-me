package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.EndPointHitDto;
import ru.practicum.mapper.EndPointHitMapper;
import ru.practicum.model.EndpointHitEntity;
import ru.practicum.service.StatisticService;

import javax.validation.Valid;
import javax.validation.constraints.Past;
import javax.validation.constraints.PastOrPresent;
import java.util.Collection;

@Slf4j
@RestController
@RequestMapping
@RequiredArgsConstructor
@Validated
public class StatisticController {
    private final StatisticService statisticService;

    @PostMapping
    public ResponseEntity<?> registerEndpointHit(@RequestBody @Valid EndPointHitDto endPointHitDto) {
        log.info("Register end point hit endPointHit = {}", endPointHitDto);
        EndpointHitEntity entity = EndPointHitMapper.toEndPointHitEntity(endPointHitDto);
        statisticService.save(entity);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<?> getBookings(@Past @RequestParam(name = "start", required = true) String start,
                                         @PastOrPresent @RequestParam(name = "end", required = true) String end,
                                         @RequestParam(name = "uris", required = false) Collection<String> uris,
                                         @RequestParam(name = "unique", required = false) String unique) {
        log.info("Get statistics from start {} to end {} with uris = {}, unique = {}", start, end, uris, unique);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
