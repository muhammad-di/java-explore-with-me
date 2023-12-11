package ru.practicum.repository.categories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.CategoryEntity;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {
}
