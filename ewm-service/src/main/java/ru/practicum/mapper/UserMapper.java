package ru.practicum.mapper;

import ru.practicum.dto.users.NewUserRequest;
import ru.practicum.dto.users.UserDto;
import ru.practicum.dto.users.UserShortDto;
import ru.practicum.model.UserEntity;


public class UserMapper {
    public static UserEntity toUserEntity(NewUserRequest dto) {
        return UserEntity.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .build();
    }

    public static UserDto toUserDto(UserEntity entity) {
        return UserDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .email(entity.getEmail())
                .build();
    }

    public static UserShortDto toUserShortDto(UserEntity entity) {
        return UserShortDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .build();
    }
}
