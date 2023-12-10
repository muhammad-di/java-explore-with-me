package ru.practicum.mapper;

import ru.practicum.dto.compilations.CompilationDto;
import ru.practicum.dto.compilations.NewCompilationDto;
import ru.practicum.dto.compilations.UpdateCompilationRequest;
import ru.practicum.dto.events.EventShortDto;
import ru.practicum.model.CompilationEntity;
import ru.practicum.model.CompilationEventEntity;

import java.util.Collection;
import java.util.stream.Collectors;


public class CompilationEventMapper {
    public static CompilationDto toCompilationDto(CompilationEntity compilationEntity,
                                                  Collection<EventShortDto> eventShortDtoList) {
        return CompilationDto.builder()
                .id(compilationEntity.getId())
                .title(compilationEntity.getTitle())
                .pinned(compilationEntity.getPinned())
                .events(eventShortDtoList)
                .build();
    }

    public static Collection<CompilationEventEntity> toCompilationEventEntityList(CompilationEntity compilation,
                                                                                  NewCompilationDto dto) {
        Collection<Long> eventIds = dto.getEvents();
        return eventIds.stream()
                .map(id -> createCompilationEventEntity(compilation, id))
                .collect(Collectors.toList());

    }

    public static Collection<CompilationEventEntity> toCompilationEventEntityList(CompilationEntity compilation,
                                                                                  UpdateCompilationRequest dto) {
        Collection<Long> eventIds = dto.getEvents();
        return eventIds.stream()
                .map(id -> createCompilationEventEntity(compilation, id))
                .collect(Collectors.toList());

    }

    public static CompilationEventEntity createCompilationEventEntity(CompilationEntity compilation, Long eventId) {

        return CompilationEventEntity.builder()
                .compilationId(compilation.getId())
                .eventId(eventId)
                .build();

    }
}
