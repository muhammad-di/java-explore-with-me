package ru.practicum.service.events.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import ru.practicum.client.StatsClient;
import ru.practicum.dto.EndPointHitDto;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.dto.events.EventFullDto;
import ru.practicum.dto.events.EventShortDto;
import ru.practicum.dto.events.UpdateEventAdminRequest;
import ru.practicum.dto.events.UpdateEventUserRequest;
import ru.practicum.exception.*;
import ru.practicum.mapper.EventMapper;
import ru.practicum.mapper.ViewStatsMapper;
import ru.practicum.model.*;
import ru.practicum.repository.categories.CategoryRepository;
import ru.practicum.repository.events.EventRepository;
import ru.practicum.repository.requests.RequestRepository;
import ru.practicum.repository.users.UserRepository;
import ru.practicum.service.events.EventService;
import ru.practicum.utils.EventsUtils;
import ru.practicum.validation.EventValidation;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final StatsClient statsClient;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final RequestRepository requestRepository;
    private final CategoryRepository categoryRepository;


    @Override
    @Transactional
    public EventEntity create(Long userId, EventEntity entity) throws UserNotFoundException,
            CategoryNotFoundException,
            EventDateInPastException,
            EventDateBeforeTwoHoursException {
        EventValidation.validate(entity);
        UserEntity initiator = findInitiatorById(userId);
        CategoryEntity category = findCategoryById(entity);

        entity.setInitiator(initiator);
        entity.setCategory(category);

        return eventRepository.save(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public EventEntity findById(Long userId, Long eventId) throws UserNotFoundException,
            EventNotFoundException,
            IncorrectInitiatorException {
        UserEntity initiator = findInitiatorById(userId);
        EventEntity entity = findEventById(eventId);
        EventValidation.validateInitiator(initiator, entity);

        return entity;
    }

    @Override
    @Transactional(readOnly = true)
    public EventFullDto findById(Long eventId, HttpServletRequest request)
            throws EventNotFoundException, EventIsNotPublishedForPublicException {
        EventEntity entity;
        EventFullDto eventFullDto;

        registerEndpointHit(request);
        entity = findEventById(eventId);
        EventValidation.validateEventIsPublished(entity);
        eventFullDto = EventMapper.toEventFullDto(entity);
        setConfirmedRequests(eventFullDto);
        setViewsForEvent(eventFullDto);

        return eventFullDto;
    }

    @Override
    @Transactional
    public EventEntity update(Long userId, Long eventId, UpdateEventUserRequest dto) throws UserNotFoundException,
            EventNotFoundException,
            IncorrectInitiatorException,
            CategoryNotFoundException,
            EventDateInPastException,
            EventDateBeforeTwoHoursException,
            EventIsAlreadyPublishedException {
        EventEntity entity;
        UserEntity initiator;
        EventEntity updatedEntity;
        CategoryEntity categoryEntity;

        entity = findEventById(eventId);
        initiator = findInitiatorById(userId);
        EventValidation.validate(initiator, entity, dto);
        updatedEntity = EventMapper.toEventEntity(entity, dto);
        categoryEntity = getCategory(entity, dto);
        updatedEntity.setCategory(categoryEntity);

        return eventRepository.save(updatedEntity);
    }

    @Override
    @Transactional
    public EventEntity update(Long eventId, UpdateEventAdminRequest dto) throws
            EventNotFoundException,
            EventDateInPastException,
            EventDateBeforeOneHourFromPublishingException, EventIsAlreadyCanceledException, EventIsAlreadyPublishedException {
        EventEntity entity = findEventById(eventId);

        EventValidation.validate(dto);
        EventValidation.validateState(entity, dto);
        EventEntity updatedEntity = EventMapper.toEventEntity(entity, dto);

        return eventRepository.save(updatedEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<EventEntity> findAll(Long userId, Integer from, Integer size) throws UserNotFoundException {
        UserEntity initiator = findInitiatorById(userId);
        Pageable sortedByIdAsc = getPagesSortedByIdAsc(from, size);

        return eventRepository.findAllByInitiatorId(initiator.getId(), sortedByIdAsc)
                .stream()
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<EventFullDto> findAll(Collection<Long> users,
                                            Collection<EventState> states,
                                            Collection<Long> categories,
                                            LocalDateTime rangeStart,
                                            LocalDateTime rangeEnd,
                                            Integer from,
                                            Integer size) {
        Pageable sortedByIdAsc = getPagesSortedByIdAsc(from, size);
        return findAllSwitcher(users, states, categories, rangeStart, rangeEnd, sortedByIdAsc)
                .stream()
                .map(EventMapper::toEventFullDto)
                .peek(this::setConfirmedRequests)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Collection<EventShortDto> search(String text,
                                            Collection<Long> categories,
                                            Boolean paid,
                                            Boolean onlyAvailable,
                                            EventSort sort,
                                            LocalDateTime rangeStart,
                                            LocalDateTime rangeEnd,
                                            Integer from,
                                            Integer size,
                                            HttpServletRequest request) throws RangeStartIsAfterRangeEndException {
        Pageable sortedByEventDateAsc;

        registerEndpointHit(request);
        sortedByEventDateAsc = getPagesSortedByEventDateAsc(from, size);
        EventValidation.validateRangeStartIsBeforeRangeEnd(rangeStart, rangeEnd);

        return searchSwitcher(text, categories, paid, onlyAvailable, sort, rangeStart, rangeEnd, sortedByEventDateAsc);
    }

    private EventEntity findEventById(Long eventId) throws EventNotFoundException {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> {
                    String message = String.format("an event with id { %d } was not found", eventId);
                    return new EventNotFoundException(message);
                });
    }

    private UserEntity findInitiatorById(Long userId) throws UserNotFoundException {
        return userRepository.findById(userId)
                .orElseThrow(() -> {
                    String message = String.format("a user with id { %d } was not found", userId);
                    return new UserNotFoundException(message);
                });
    }

    private CategoryEntity findCategoryById(EventEntity entity) throws CategoryNotFoundException {
        Long catId = entity.getCategory().getId();
        return categoryRepository.findById(catId)
                .orElseThrow(() -> {
                    String message = String.format("a category with id { %d } was not found", catId);
                    return new CategoryNotFoundException(message);
                });
    }

    private CategoryEntity findCategoryById(UpdateEventUserRequest dto) throws CategoryNotFoundException {
        Long catId = dto.getCategory();
        return categoryRepository.findById(catId)
                .orElseThrow(() -> {
                    String message = String.format("a category with id { %d } was not found", catId);
                    return new CategoryNotFoundException(message);
                });
    }

    private CategoryEntity getCategory(EventEntity entity, UpdateEventUserRequest dto) throws CategoryNotFoundException {
        if (dto.getCategory() == null) {
            return entity.getCategory();
        }
        return findCategoryById(dto);
    }

    private Pageable getPagesSortedByIdAsc(int from, int size) {
        return PageRequest.of(from, size, Sort.by("id").ascending());
    }

    private Pageable getPagesSortedByEventDateAsc(int from, int size) {
        return PageRequest.of(from, size, Sort.by("eventDate").ascending());
    }


    private Page<EventEntity> findAllSwitcher(Collection<Long> users,
                                              Collection<EventState> states,
                                              Collection<Long> categories,
                                              LocalDateTime rangeStart,
                                              LocalDateTime rangeEnd,
                                              Pageable sortedByIdAsc) {
        if (CollectionUtils.isEmpty(users)
                && CollectionUtils.isEmpty(states)
                && CollectionUtils.isEmpty(categories)
                && ObjectUtils.isEmpty(rangeStart)
                && ObjectUtils.isEmpty(rangeEnd)) {

            return eventRepository.findAll(sortedByIdAsc);

        } else if (CollectionUtils.isEmpty(states)
                && CollectionUtils.isEmpty(categories)
                && ObjectUtils.isEmpty(rangeStart)
                && ObjectUtils.isEmpty(rangeEnd)) {

            return eventRepository.findAllForAdmin(users, sortedByIdAsc);

        } else if (CollectionUtils.isEmpty(categories)
                && ObjectUtils.isEmpty(rangeStart)
                && ObjectUtils.isEmpty(rangeEnd)) {

            return eventRepository.findAllForAdmin(users, states, sortedByIdAsc);

        } else if (ObjectUtils.isEmpty(rangeStart)
                && ObjectUtils.isEmpty(rangeEnd)) {

            return eventRepository.findAllForAdmin(users, states, categories, sortedByIdAsc);

        }

        return eventRepository.findAllForAdmin(users, states, categories, rangeStart, rangeEnd, sortedByIdAsc);
    }

    private void setConfirmedRequests(EventFullDto eventFullDto) {
        long eventId;
        long confirmedRequests;

        eventId = eventFullDto.getId();
        confirmedRequests = countConfirmedRequestsForEvent(eventId);
        eventFullDto.setConfirmedRequests(confirmedRequests);
    }

    private void setViewsForEvent(EventFullDto eventFullDto) {
        long eventId;
        long views;

        eventId = eventFullDto.getId();
        views = countViewsForEvent(eventId);
        eventFullDto.setViews(views);
    }

    private long countConfirmedRequestsForEvent(long eventId) {
        RequestStatus status;

        status = RequestStatus.CONFIRMED;
        return requestRepository.countByEventIdAndStatus(eventId, status);
    }

    private long countViewsForEvent(long eventId) {
        String path;
        Object body;
        ViewStatsDto viewStatsDto;
        ResponseEntity<Object> response;
        Collection<ViewStatsDto> viewStatsDtoList;
        LocalDateTime end = EventsUtils.getDefaultEndForEndpointHit();
        LocalDateTime start = EventsUtils.getDefaultStartForEndpointHit();

        path = "/events/".concat(String.valueOf(eventId));
        response = statsClient.get(start, end, List.of(path), true);
        body = response.getBody();
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

//    Register EndpointHit----------------------------------------------------------------------------------------------

    private void registerEndpointHit(HttpServletRequest request) {
        EndPointHitDto body = EndPointHitDto.builder()
                .app(EventsUtils.getAppName())
                .uri(request.getRequestURI())
                .ip(request.getRemoteAddr())
                .timestamp(EventsUtils.getEndpointHitTimestamp())
                .build();

        statsClient.post(body);
    }


    //    "/event"
    private Collection<EventEntity> filterAvailableForSearch(Collection<EventEntity> eventEntityPage, Boolean onlyAvailable) {
        if (!onlyAvailable) {
            return eventEntityPage;
        }
        return eventEntityPage.stream()
                .filter(this::isEventAvailable)
                .collect(Collectors.toList());
    }

    private Collection<EventShortDto> sortForSearch(Collection<EventEntity> eventEntities, EventSort sort) {
        EventSort sortByViews = EventSort.VIEWS;
        Collection<EventShortDto> eventShortDtoList;
        Comparator<EventShortDto> compareByViews = EventsUtils.getCompareByViews();

        eventShortDtoList = setConfirmedRequestsForEventEntities(eventEntities);
        setViewsForEventShortDtoList(eventShortDtoList);

        if (sort.equals(sortByViews)) {
            return eventShortDtoList.stream()
                    .sorted(compareByViews)
                    .collect(Collectors.toList());
        }
        return eventShortDtoList;
    }

    private boolean isEventAvailable(EventEntity eventEntity) {
        long eventId;
        long participantLimit;
        long confirmedRequests;

        eventId = eventEntity.getId();
        participantLimit = eventEntity.getParticipantLimit();
        confirmedRequests = countConfirmedRequestsForEvent(eventId);

        return participantLimit > confirmedRequests;
    }


    private Collection<EventShortDto> setConfirmedRequestsForEventEntities(Collection<EventEntity> eventEntities) {
        return eventEntities.stream()
                .map(EventMapper::toEventShortDto)
                .peek(e -> e.setConfirmedRequests(countConfirmedRequestsForEvent(e.getId())))
                .collect(Collectors.toList());
    }

    private void setViewsForEventShortDtoList(Collection<EventShortDto> dtoList) {
        dtoList.stream().peek(e -> e.setViews(countViewsForEvent(e.getId())));
    }


    private Collection<EventShortDto> searchSwitcher(String text,
                                                     Collection<Long> categories,
                                                     Boolean paid,
                                                     Boolean onlyAvailable,
                                                     EventSort sort,
                                                     LocalDateTime rangeStart,
                                                     LocalDateTime rangeEnd,
                                                     Pageable sortedByEventDateAsc) {
        EventState state = EventState.PUBLISHED;
        Collection<EventEntity> eventEntityList;
        Collection<EventEntity> filteredEventEntities;

        if (ObjectUtils.isEmpty(text)
                && CollectionUtils.isEmpty(categories)
                && ObjectUtils.isEmpty(paid)
                && ObjectUtils.isEmpty(onlyAvailable)
                && ObjectUtils.isEmpty(sort)
                && ObjectUtils.isEmpty(rangeStart)
                && ObjectUtils.isEmpty(rangeEnd)) {

            eventEntityList = findListForSearchNoFilters(state, sortedByEventDateAsc);
            return sortForSearch(eventEntityList, EventSort.EVENT_DATE);


        } else if (ObjectUtils.isEmpty(text)
                && ObjectUtils.isEmpty(paid)
                && ObjectUtils.isEmpty(onlyAvailable)
                && ObjectUtils.isEmpty(sort)
                && ObjectUtils.isEmpty(rangeStart)
                && ObjectUtils.isEmpty(rangeEnd)) {

            eventEntityList = findListForSearchCategoriesFilter(state, categories, sortedByEventDateAsc);
            return sortForSearch(eventEntityList, EventSort.EVENT_DATE);


        } else if (CollectionUtils.isEmpty(categories)
                && ObjectUtils.isEmpty(paid)
                && ObjectUtils.isEmpty(onlyAvailable)
                && ObjectUtils.isEmpty(sort)
                && ObjectUtils.isEmpty(rangeStart)
                && ObjectUtils.isEmpty(rangeEnd)) {

            eventEntityList = findListForSearchTextFilter(text, state, sortedByEventDateAsc);
            return sortForSearch(eventEntityList, EventSort.EVENT_DATE);


        } else if (ObjectUtils.isEmpty(paid)
                && ObjectUtils.isEmpty(onlyAvailable)
                && ObjectUtils.isEmpty(sort)
                && ObjectUtils.isEmpty(rangeStart)
                && ObjectUtils.isEmpty(rangeEnd)) {

            eventEntityList = findListForSearchTextCategoriesFilters(text, state, categories, sortedByEventDateAsc);
            return sortForSearch(eventEntityList, EventSort.EVENT_DATE);
        } else if (ObjectUtils.isEmpty(onlyAvailable)
                && ObjectUtils.isEmpty(sort)
                && ObjectUtils.isEmpty(rangeStart)
                && ObjectUtils.isEmpty(rangeEnd)) {

            eventEntityList = findListForSearchTextCategoriesPaidFilters(text, state, categories, paid, sortedByEventDateAsc);
            return sortForSearch(eventEntityList, EventSort.EVENT_DATE);
        }

        eventEntityList = findListForSearchAllFilters(text, state, categories, paid, rangeStart, rangeEnd, sortedByEventDateAsc);
        filteredEventEntities = filterAvailableForSearch(eventEntityList, onlyAvailable);
        return sortForSearch(filteredEventEntities, sort);
    }

    private Collection<EventEntity> findListForSearchNoFilters(EventState state, Pageable sortedByEventDateAsc) {

        return eventRepository.search(state, EventsUtils.getDefaultRangeStart(), sortedByEventDateAsc);
    }

    private Collection<EventEntity> findListForSearchTextFilter(String text, EventState state,
                                                                Pageable sortedByEventDateAsc) {

        return eventRepository.search(text, state, EventsUtils.getDefaultRangeStart(), sortedByEventDateAsc);
    }

    private Collection<EventEntity> findListForSearchCategoriesFilter(EventState state,
                                                                      Collection<Long> categories,
                                                                      Pageable sortedByEventDateAsc) {

        return eventRepository.search(state, categories, EventsUtils.getDefaultRangeStart(), sortedByEventDateAsc);
    }


    private Collection<EventEntity> findListForSearchTextCategoriesFilters(String text, EventState state,
                                                                           Collection<Long> categories,
                                                                           Pageable sortedByEventDateAsc) {

        return eventRepository.search(text, state, categories, EventsUtils.getDefaultRangeStart(), sortedByEventDateAsc);
    }

    private Collection<EventEntity> findListForSearchTextCategoriesPaidFilters(String text, EventState state,
                                                                               Collection<Long> categories,
                                                                               Boolean paid,
                                                                               Pageable sortedByEventDateAsc) {

        return eventRepository.search(text, state, categories, paid, EventsUtils.getDefaultRangeStart(), sortedByEventDateAsc);
    }

    private Collection<EventEntity> findListForSearchAllFilters(String text, EventState state,
                                                                Collection<Long> categories,
                                                                Boolean paid,
                                                                LocalDateTime rangeStart,
                                                                LocalDateTime rangeEnd,
                                                                Pageable sortedByEventDateAsc) {

        return eventRepository.search(text, state, categories, paid, rangeStart, rangeEnd, sortedByEventDateAsc);
    }
}
