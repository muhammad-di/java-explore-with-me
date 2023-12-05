package ru.practicum.controller.admin.users;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.users.NewUserRequest;
import ru.practicum.dto.users.UserDto;
import ru.practicum.exception.UserNotFoundException;
import ru.practicum.mapper.UserMapper;
import ru.practicum.model.UserEntity;
import ru.practicum.service.users.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;
import java.util.stream.Collectors;


@RestController
@RequestMapping(path = "/admin/users")
@RequiredArgsConstructor
@Validated
public class AdminUserController {
    private final UserService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto create(@Valid @RequestBody NewUserRequest newUserRequest) {
        UserEntity entity = UserMapper.toUserEntity(newUserRequest);
        entity = service.create(entity);

        return UserMapper.toUserDto(entity);
    }

    @DeleteMapping(path = "/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@Positive @PathVariable(name = "userId") Long userId) throws UserNotFoundException {
        service.delete(userId);
    }

    @GetMapping
    public Collection<UserDto> findAll(@RequestParam(name = "ids", required = false) Collection<Long> ids,
                                       @PositiveOrZero
                                       @RequestParam(name = "from", defaultValue = "0") Integer from,
                                       @Positive
                                       @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return service.findAll(ids, from, size)
                .stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

}
