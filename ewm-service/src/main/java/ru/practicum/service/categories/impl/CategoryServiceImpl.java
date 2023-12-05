package ru.practicum.service.categories.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exception.CategoryNotFoundException;
import ru.practicum.model.CategoryEntity;
import ru.practicum.repository.categories.CategoryRepository;
import ru.practicum.service.categories.CategoryService;

import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository repository;

    @Override
    @Transactional
    public CategoryEntity create(CategoryEntity entity) {
        return repository.save(entity);
    }

    @Override
    @Transactional
    public CategoryEntity update(Long catId, CategoryEntity entity) throws CategoryNotFoundException {
        findById(catId);
        entity.setId(catId);
        return repository.save(entity);
    }

    @Override
    @Transactional
    public void delete(Long catId) throws CategoryNotFoundException {
        findById(catId);
        repository.deleteById(catId);
    }


    @Override
    @Transactional(readOnly = true)
    public CategoryEntity findById(Long catId) throws CategoryNotFoundException {
        return repository.findById(catId)
                .orElseThrow(() -> {
                    String message = String.format("a category with id { %d } was not found", catId);
                    return new CategoryNotFoundException(message);
                });
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<CategoryEntity> findAll(Integer from, Integer size) {
        Pageable sortedByIdAsc = getPagesSortedByIdAsc(from, size);
        return repository.findAll(sortedByIdAsc)
                .stream()
                .collect(Collectors.toList());
    }


    private Pageable getPagesSortedByIdAsc(int from, int size) {
        return PageRequest.of(from, size, Sort.by("id").ascending());
    }
}
