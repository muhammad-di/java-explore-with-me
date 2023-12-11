package ru.practicum.controller.admin.compilations;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.compilations.CompilationDto;
import ru.practicum.dto.compilations.NewCompilationDto;
import ru.practicum.dto.compilations.UpdateCompilationRequest;
import ru.practicum.exception.CompilationNotFoundException;
import ru.practicum.service.compilations.CompilationService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RestController
@RequestMapping(path = "/admin/compilations")
@RequiredArgsConstructor
@Validated
public class AdminCompilationController {

    private final CompilationService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto create(@Valid @RequestBody NewCompilationDto dto) {
        return service.create(dto);
    }

    @DeleteMapping(path = "/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@Positive @PathVariable(name = "compId") Long compId) throws CompilationNotFoundException {
        service.delete(compId);
    }

    @PatchMapping(path = "/{compId}")
    public CompilationDto update(@Positive @PathVariable(name = "compId") Long catId,
                              @Valid @RequestBody UpdateCompilationRequest dto) throws CompilationNotFoundException {
        return service.update(catId, dto);
    }
}
