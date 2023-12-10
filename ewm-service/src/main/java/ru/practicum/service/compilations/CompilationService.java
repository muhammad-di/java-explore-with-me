package ru.practicum.service.compilations;

import ru.practicum.dto.compilations.CompilationDto;
import ru.practicum.dto.compilations.NewCompilationDto;
import ru.practicum.dto.compilations.UpdateCompilationRequest;
import ru.practicum.exception.CompilationNotFoundException;

import java.util.Collection;

public interface CompilationService {
    CompilationDto create(NewCompilationDto dto);

    CompilationDto findById(Long compId) throws CompilationNotFoundException;

    void delete(Long compId) throws CompilationNotFoundException;

    CompilationDto update(Long catId, UpdateCompilationRequest dto) throws CompilationNotFoundException;

    Collection<CompilationDto> findAll(Boolean pinned, Integer from, Integer size);
}
