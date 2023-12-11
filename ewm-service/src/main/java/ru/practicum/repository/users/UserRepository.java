package ru.practicum.repository.users;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.UserEntity;

import java.util.Collection;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Page<UserEntity> findAllByIdIn(Collection<Long> ids, Pageable pageable);
}
