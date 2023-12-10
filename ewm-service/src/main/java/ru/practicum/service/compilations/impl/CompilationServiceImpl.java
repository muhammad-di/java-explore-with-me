package ru.practicum.service.compilations.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import ru.practicum.client.StatsClient;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.dto.compilations.CompilationDto;
import ru.practicum.dto.compilations.NewCompilationDto;
import ru.practicum.dto.compilations.UpdateCompilationRequest;
import ru.practicum.dto.events.EventShortDto;
import ru.practicum.exception.CompilationNotFoundException;
import ru.practicum.mapper.CompilationEventMapper;
import ru.practicum.mapper.CompilationMapper;
import ru.practicum.mapper.EventMapper;
import ru.practicum.mapper.ViewStatsMapper;
import ru.practicum.model.CompilationEntity;
import ru.practicum.model.CompilationEventEntity;
import ru.practicum.model.EventEntity;
import ru.practicum.model.RequestStatus;
import ru.practicum.repository.compilations.CompilationEventRepository;
import ru.practicum.repository.compilations.CompilationRepository;
import ru.practicum.repository.events.EventRepository;
import ru.practicum.repository.requests.RequestRepository;
import ru.practicum.service.compilations.CompilationService;
import ru.practicum.utils.EventsUtils;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    private final StatsClient statsClient;
    private final RequestRepository requestRepository;
    private final EventRepository eventRepository;
    private final CompilationRepository compilationRepository;
    private final CompilationEventRepository compilationEventRepository;


    @Override
    @Transactional
    public CompilationDto create(NewCompilationDto dto) {
        Collection<Long> ids;
        CompilationEntity compilationEntity;
        CompilationEntity compilationEntityFromDB;
        Collection<EventEntity> eventEntities;
        Collection<EventShortDto> eventShortDtoList;
        Collection<CompilationEventEntity> compilationEventEntityList;

        ids = dto.getEvents();
        compilationEntity = CompilationMapper.toCategoryEntity(dto);
        compilationEntityFromDB = compilationRepository.save(compilationEntity);
        if (CollectionUtils.isEmpty(ids)) {
            return CompilationEventMapper.toCompilationDto(compilationEntityFromDB, Collections.emptyList());
        }
        compilationEventEntityList = CompilationEventMapper.toCompilationEventEntityList(compilationEntityFromDB, dto);
        createCompilationEventEntity(compilationEventEntityList);
        eventEntities = findEventsByIds(ids);
        eventShortDtoList = EventMapper.toEventShortDtoList(eventEntities);
        setConfirmedRequestsForList(eventShortDtoList);
        setViewsList(eventShortDtoList);

        return CompilationEventMapper.toCompilationDto(compilationEntityFromDB, eventShortDtoList);
    }


    @Override
    @Transactional
    public CompilationDto update(Long compId, UpdateCompilationRequest dto) throws CompilationNotFoundException {
        Collection<Long> ids;
        CompilationEntity compilationEntity;
        CompilationEntity foundFromDBCompilationEntity;
        CompilationEntity compilationEntityFromDB;
        Collection<EventEntity> eventEntities;
        Collection<EventShortDto> eventShortDtoList;
        Collection<CompilationEventEntity> compilationEventEntityList;

        ids = dto.getEvents();
        foundFromDBCompilationEntity = findCompilationById(compId);
        compilationEntity = CompilationMapper.toCategoryEntity(foundFromDBCompilationEntity, dto);
        compilationEntity.setId(compId);
        compilationEntityFromDB = compilationRepository.save(compilationEntity);
        compilationEventEntityList = findCompilationEventEntityListByCompilationId(compId);
        if (CollectionUtils.isEmpty(ids) && CollectionUtils.isEmpty(compilationEventEntityList)) {
            return CompilationEventMapper.toCompilationDto(compilationEntityFromDB, Collections.emptyList());
        } else if (CollectionUtils.isEmpty(ids)) {
            deleteCompilationEventList(compilationEventEntityList);
            return CompilationEventMapper.toCompilationDto(compilationEntityFromDB, Collections.emptyList());
        }
        deleteCompilationEventList(compilationEventEntityList);
        compilationEventEntityList = CompilationEventMapper.toCompilationEventEntityList(compilationEntityFromDB, dto);
        createCompilationEventEntity(compilationEventEntityList);
        eventEntities = findEventsByIds(ids);
        eventShortDtoList = EventMapper.toEventShortDtoList(eventEntities);
        setConfirmedRequestsForList(eventShortDtoList);
        setViewsList(eventShortDtoList);

        return CompilationEventMapper.toCompilationDto(compilationEntityFromDB, eventShortDtoList);
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<CompilationDto> findAll(Boolean pinned, Integer from, Integer size) {
        Pageable sortedByIdAsc;
        Collection<CompilationEntity> compilationEntities;

        sortedByIdAsc = getPagesSortedByIdAsc(from, size);
        if (ObjectUtils.isEmpty(pinned)) {
            compilationEntities = compilationRepository
                    .findAll(sortedByIdAsc)
                    .stream()
                    .collect(Collectors.toList());
        } else {
            compilationEntities = compilationRepository.findAllByPinned(pinned, sortedByIdAsc);
        }

        return compilationEntities.stream()
                .map(this::getCompilationDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CompilationDto findById(Long compId) throws CompilationNotFoundException {
        CompilationEntity compilationEntity;
        Collection<EventEntity> eventEntities;
        Collection<EventShortDto> eventShortDtoList;
        Collection<CompilationEventEntity> compilationEventEntityList;

        compilationEntity = findCompilationById(compId);
        compilationEventEntityList = findCompilationEventEntityListByCompilationId(compId);
        if (CollectionUtils.isEmpty(compilationEventEntityList)) {
            return CompilationEventMapper.toCompilationDto(compilationEntity, Collections.emptyList());
        }
        eventEntities = findEventsByCompilationEventEntityList(compilationEventEntityList);
        eventShortDtoList = EventMapper.toEventShortDtoList(eventEntities);
        setConfirmedRequestsForList(eventShortDtoList);
        setViewsList(eventShortDtoList);

        return CompilationEventMapper.toCompilationDto(compilationEntity, eventShortDtoList);
    }

    @Override
    @Transactional
    public void delete(Long compId) throws CompilationNotFoundException {
        CompilationEntity compilationEntity;
        Collection<CompilationEventEntity> compilationEventEntityList;

        compilationEntity = findCompilationById(compId);
        compilationEventEntityList = findCompilationEventEntityListByCompilationId(compId);
        deleteCompilationEventList(compilationEventEntityList);
        compilationRepository.delete(compilationEntity);

    }

    private CompilationDto getCompilationDto(CompilationEntity compilationEntity) {
        Long compId = compilationEntity.getId();
        Collection<EventEntity> eventEntities;
        Collection<EventShortDto> eventShortDtoList;
        Collection<CompilationEventEntity> compilationEventEntityList;

        compilationEventEntityList = findCompilationEventEntityListByCompilationId(compId);
        eventEntities = findEventsByCompilationEventEntityList(compilationEventEntityList);
        eventShortDtoList = EventMapper.toEventShortDtoList(eventEntities);
        setConfirmedRequestsForList(eventShortDtoList);
        setViewsList(eventShortDtoList);

        return CompilationEventMapper.toCompilationDto(compilationEntity, eventShortDtoList);
    }

    private Pageable getPagesSortedByIdAsc(int from, int size) {
        return PageRequest.of(from, size, Sort.by("id").ascending());
    }

    private void deleteCompilationEventList(Collection<CompilationEventEntity> compilationEventEntity) {
        compilationEventEntity.forEach(compilationEventRepository::delete);
    }

    private Collection<CompilationEventEntity> findCompilationEventEntityListByCompilationId(Long compId) {
        return compilationEventRepository.findAllByCompilationId(compId);
    }


    public CompilationEntity findCompilationById(Long compId) throws CompilationNotFoundException {
        return compilationRepository.findById(compId)
                .orElseThrow(() -> {
                    String message = String.format("a compilation with id { %d } was not found", compId);
                    return new CompilationNotFoundException(message);
                });
    }


    private void createCompilationEventEntity(Collection<CompilationEventEntity> compilations) {
        compilations.forEach(compilationEventRepository::save);
    }

    private Collection<EventEntity> findEventsByIds(Collection<Long> ids) {
        return eventRepository.findAllByIdIn(ids);
    }

    private Collection<EventEntity> findEventsByCompilationEventEntityList(
            Collection<CompilationEventEntity> compilationEventEntityList) {
        Collection<Long> eventEntities = compilationEventEntityList.stream()
                .map(CompilationEventEntity::getEventId)
                .collect(Collectors.toList());
        return findEventsByIds(eventEntities);
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

    private void setConfirmedRequestsForList(Collection<EventShortDto> dtoList) {
        dtoList.forEach(this::setConfirmedRequests);
    }

    private void setViewsList(Collection<EventShortDto> dtoList) {
        dtoList.forEach(this::setViewsForEvent);
    }


}
