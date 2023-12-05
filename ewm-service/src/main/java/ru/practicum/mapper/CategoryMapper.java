package ru.practicum.mapper;

import ru.practicum.dto.categories.CategoryDto;
import ru.practicum.dto.categories.NewCategoryDto;
import ru.practicum.model.CategoryEntity;


public class CategoryMapper {
    public static CategoryEntity toCategoryEntity(NewCategoryDto dto) {
        return CategoryEntity.builder()
                .name(dto.getName())
                .build();
    }

    public static CategoryDto toCategoryDto(CategoryEntity entity) {
        return CategoryDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .build();
    }
}
