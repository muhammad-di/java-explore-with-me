package ru.practicum.mapper;

import ru.practicum.dto.compilations.NewCompilationDto;
import ru.practicum.dto.compilations.UpdateCompilationRequest;
import ru.practicum.model.CompilationEntity;


public class CompilationMapper {
    public static CompilationEntity toCategoryEntity(NewCompilationDto dto) {
        return CompilationEntity.builder()
                .title(dto.getTitle())
                .pinned(dto.isPinned())
                .build();
    }

    public static CompilationEntity toCategoryEntity(CompilationEntity compilationEntity, UpdateCompilationRequest dto) {
        return CompilationEntity.builder()
                .title(dto.getTitle() == null ? compilationEntity.getTitle() : dto.getTitle())
                .pinned(dto.getPinned() == null ? compilationEntity.getPinned() : dto.getPinned())
                .build();
    }
}



