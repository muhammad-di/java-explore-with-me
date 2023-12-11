package ru.practicum.controller.categories;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.categories.CategoryDto;
import ru.practicum.exception.CategoryNotFoundException;
import ru.practicum.mapper.CategoryMapper;
import ru.practicum.model.CategoryEntity;
import ru.practicum.service.categories.CategoryService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/categories")
@RequiredArgsConstructor
@Validated
public class CategoryController {
    private final CategoryService service;

    @GetMapping(path = "/{catId}")
    public CategoryDto findById(@Positive @PathVariable(name = "catId") Long catId) throws CategoryNotFoundException {
        CategoryEntity entity = service.findById(catId);
        return CategoryMapper.toCategoryDto(entity);
    }

    @GetMapping
    public Collection<CategoryDto> findAll(@PositiveOrZero
                                           @RequestParam(name = "from", defaultValue = "0") Integer from,
                                           @Positive
                                           @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return service.findAll(from, size)
                .stream()
                .map(CategoryMapper::toCategoryDto)
                .collect(Collectors.toList());
    }
}
