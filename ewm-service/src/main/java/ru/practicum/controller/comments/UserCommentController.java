package ru.practicum.controller.comments;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.comments.CommentDto;
import ru.practicum.dto.comments.NewCommentDto;
import ru.practicum.exception.*;
import ru.practicum.mapper.CommentMapper;
import ru.practicum.model.CommentEntity;
import ru.practicum.service.comments.CommentsService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Validated
public class UserCommentController {
    private final CommentsService service;


    @PostMapping(path = "/{userId}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto create(@Positive @PathVariable(name = "userId") Long commenterId,
                             @Positive @RequestParam(name = "eventId") Long eventId,
                             @Valid @RequestBody NewCommentDto dto)
            throws UserNotFoundException,
            EventNotFoundException,
            EventIsNotPublishedException,
            RequesterAndInitiatorAreSameException,
            CommenterAndInitiatorAreSameException {
        CommentEntity commentEntity = CommentMapper.toCommentEntity(dto);

        return service.create(commenterId, eventId, commentEntity);
    }
}
