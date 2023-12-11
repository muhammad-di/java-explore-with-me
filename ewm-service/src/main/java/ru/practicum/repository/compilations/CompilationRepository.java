package ru.practicum.repository.compilations;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.CompilationEntity;
import ru.practicum.repository.compilations.dao.CustomizedCompilationRepository;

import java.util.List;

public interface CompilationRepository extends JpaRepository<CompilationEntity, Long>, CustomizedCompilationRepository {
    List<CompilationEntity> findAllByPinned(Boolean pinned, Pageable sortedByIdAsc);
}
