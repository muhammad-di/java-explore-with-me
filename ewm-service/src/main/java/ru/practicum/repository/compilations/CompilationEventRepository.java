package ru.practicum.repository.compilations;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.CompilationEventEntity;

import java.util.Collection;

public interface CompilationEventRepository extends JpaRepository<CompilationEventEntity, Long> {
    Collection<CompilationEventEntity> findAllByCompilationId(Long compId);
}
