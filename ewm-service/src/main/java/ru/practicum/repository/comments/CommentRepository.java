package ru.practicum.repository.comments;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.CommentEntity;


public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
}
