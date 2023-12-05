package ru.practicum.controller.admin.categories;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.categories.CategoryDto;
import ru.practicum.dto.categories.NewCategoryDto;
import ru.practicum.exception.CategoryNotFoundException;
import ru.practicum.mapper.CategoryMapper;
import ru.practicum.model.CategoryEntity;
import ru.practicum.service.categories.CategoryService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RestController
@RequestMapping(path = "/admin/categories")
@RequiredArgsConstructor
@Validated
public class AdminCategoryController {

    private final CategoryService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto create(@Valid @RequestBody NewCategoryDto dto) {
        CategoryEntity entity = CategoryMapper.toCategoryEntity(dto);
        entity = service.create(entity);

        return CategoryMapper.toCategoryDto(entity);
    }

    @DeleteMapping(path = "/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@Positive @PathVariable(name = "catId") Long catId) throws CategoryNotFoundException {
        service.delete(catId);
    }

    @PatchMapping(path = "/{catId}")
    public CategoryDto update(@Positive @PathVariable(name = "catId") Long catId,
                              @Valid @RequestBody NewCategoryDto dto) throws CategoryNotFoundException {
        CategoryEntity entity = CategoryMapper.toCategoryEntity(dto);
        entity = service.update(catId, entity);

        return CategoryMapper.toCategoryDto(entity);
    }
}
