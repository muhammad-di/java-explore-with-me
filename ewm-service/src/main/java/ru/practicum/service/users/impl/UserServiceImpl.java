package ru.practicum.service.users.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import ru.practicum.exception.UserNotFoundException;
import ru.practicum.model.UserEntity;
import ru.practicum.repository.users.UserRepository;
import ru.practicum.service.users.UserService;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Override
    @Transactional
    public UserEntity create(UserEntity entity) {
        return repository.save(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<UserEntity> findAll(Collection<Long> ids, Integer from, Integer size) {
        Pageable sortedByIdAsc = getPagesSortedByIdAsc(from, size);
        if (CollectionUtils.isEmpty(ids)) {
            return repository.findAll(sortedByIdAsc)
                    .stream()
                    .collect(Collectors.toList());
        }
        return repository.findAllByIdIn(ids, sortedByIdAsc)
                .stream()
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void delete(Long userId) throws UserNotFoundException {
        findById(userId);
        repository.deleteById(userId);
    }

    @Transactional(readOnly = true)
    private UserEntity findById(Long userId) throws UserNotFoundException {
        return repository.findById(userId)
                .orElseThrow(() -> {
                    String message = String.format("a user with id { %d } was not found", userId);
                    return new UserNotFoundException(message);
                });
    }

    private Pageable getPagesSortedByIdAsc(int from, int size) {
        return PageRequest.of(from, size, Sort.by("id").ascending());
    }
}
