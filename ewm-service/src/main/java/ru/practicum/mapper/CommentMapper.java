package ru.practicum.mapper;

import ru.practicum.dto.comments.CommentDto;
import ru.practicum.dto.comments.NewCommentDto;
import ru.practicum.dto.events.EventShortDto;
import ru.practicum.model.CommentEntity;
import ru.practicum.model.CommentState;


public class CommentMapper {
    public static CommentEntity toCommentEntity(NewCommentDto dto) {
        return CommentEntity.builder()
                .text(dto.getText())
                .state(CommentState.PUBLISHED)
                .build();
    }

    public static CommentDto toCommentDto(CommentEntity commentEntity, EventShortDto eventShortDto) {
        return CommentDto.builder()
                .id(commentEntity.getId())
                .created(commentEntity.getCreated())
                .event(eventShortDto)
                .state(commentEntity.getState())
                .text(commentEntity.getText())
                .build();
    }
}
