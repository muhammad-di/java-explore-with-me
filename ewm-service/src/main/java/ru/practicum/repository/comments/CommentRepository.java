package ru.practicum.repository.comments;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.CommentEntity;

import java.util.Collection;


public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    Collection<CommentEntity> findAllByEventIdOrderByCreatedDesc(Long eventId);
}
