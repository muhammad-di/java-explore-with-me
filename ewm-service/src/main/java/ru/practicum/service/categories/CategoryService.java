package ru.practicum.service.categories;

import ru.practicum.exception.CategoryNotFoundException;
import ru.practicum.model.CategoryEntity;

import java.util.Collection;

public interface CategoryService {
    CategoryEntity create(CategoryEntity entity);

    CategoryEntity update(Long catId, CategoryEntity entity) throws CategoryNotFoundException;

    void delete(Long catId) throws CategoryNotFoundException;

    CategoryEntity findById(Long catId) throws CategoryNotFoundException;

    Collection<CategoryEntity> findAll(Integer from, Integer size);

}
