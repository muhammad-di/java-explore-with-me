package ru.practicum.service.users;

import ru.practicum.exception.UserNotFoundException;
import ru.practicum.model.UserEntity;

import java.util.Collection;

public interface UserService {

    UserEntity create(UserEntity entity);

    Collection<UserEntity> findAll(Collection<Long> ids, Integer from, Integer size);

    void delete(Long userId) throws UserNotFoundException;
}
