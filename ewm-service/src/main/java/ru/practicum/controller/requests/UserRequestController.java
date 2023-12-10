package ru.practicum.controller.requests;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.requests.EventRequestStatusUpdateRequest;
import ru.practicum.dto.requests.EventRequestStatusUpdateResult;
import ru.practicum.dto.requests.ParticipationRequestDto;
import ru.practicum.exception.*;
import ru.practicum.mapper.RequestMapper;
import ru.practicum.model.RequestEntity;
import ru.practicum.service.requests.RequestService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.Collection;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Validated
public class UserRequestController {
    private final RequestService service;


    @PostMapping(path = "/{userId}/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto create(@Positive @PathVariable(name = "userId") Long userId,
                                          @Valid @Positive @RequestParam(name = "eventId") Long eventId)
            throws UserNotFoundException,
            RequesterAndInitiatorAreSameException,
            EventIsNotPublishedException,
            EventNotFoundException,
            ParticipationLimitReachedException,
            RequestAlreadyExistsException {
        RequestEntity entity = service.create(userId, eventId);

        return RequestMapper.toParticipationRequestDto(entity);
    }

    @GetMapping(path = "/{userId}/requests")
    public Collection<ParticipationRequestDto> findById(@Positive @PathVariable(name = "userId") Long userId)
            throws UserNotFoundException {

        return service.findAll(userId).stream()
                .map(RequestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    @PatchMapping(path = "/{userId}/requests/{requestId}/cancel")
    public ParticipationRequestDto update(@Positive @PathVariable(name = "userId") Long userId,
                                          @Positive @PathVariable(name = "requestId") Long requestId)
            throws UserNotFoundException,
            RequestNotFoundException,
            RequesterAndClaimedRequesterAreNotSameException {
        RequestEntity entity = service.cancel(userId, requestId);

        return RequestMapper.toParticipationRequestDto(entity);
    }

    @GetMapping(path = "/{userId}/events/{eventId}/requests")
    public Collection<ParticipationRequestDto> findRequestsForEvent(@Positive @PathVariable(name = "userId") Long userId,
                                                                    @Positive @PathVariable(name = "eventId") Long eventId)
            throws UserNotFoundException, IncorrectInitiatorException, EventNotFoundException {

        return service.findRequestsForEvent(userId, eventId).stream()
                .map(RequestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    @PatchMapping(path = "/{userId}/events/{eventId}/requests")
    public EventRequestStatusUpdateResult updateStatus(@Positive @PathVariable(name = "userId") Long userId,
                                                       @Positive @PathVariable(name = "eventId") Long eventId,
                                                       @RequestBody EventRequestStatusUpdateRequest requestBody)
            throws UserNotFoundException,
            IncorrectInitiatorException,
            EventNotFoundException,
            RequestIsNotPendingException,
            ParticipationLimitReachedException {

        return service.updateRequestsStatus(userId, eventId, requestBody);
    }
}
