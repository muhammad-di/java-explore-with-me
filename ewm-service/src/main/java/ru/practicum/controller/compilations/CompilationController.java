package ru.practicum.controller.compilations;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.compilations.CompilationDto;
import ru.practicum.exception.CompilationNotFoundException;
import ru.practicum.exception.RangeStartIsAfterRangeEndException;
import ru.practicum.service.compilations.CompilationService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;


@RestController
@RequestMapping(path = "/compilations")
@RequiredArgsConstructor
@Validated
public class CompilationController {

    private final CompilationService service;


    @GetMapping
    public Collection<CompilationDto> findAll(@RequestParam(name = "pinned", required = false) Boolean pinned,
                                              @PositiveOrZero
                                              @RequestParam(name = "from", defaultValue = "0") Integer from,
                                              @Positive
                                              @RequestParam(name = "size", defaultValue = "10") Integer size)
            throws RangeStartIsAfterRangeEndException {
        return service.findAll(pinned, from, size);
    }

    @GetMapping(path = "/{compId}")
    public CompilationDto findById(@Positive @PathVariable(name = "compId") Long compId)
            throws CompilationNotFoundException {
        return service.findById(compId);
    }
}
