package ru.practicum.service.comments;

import ru.practicum.dto.comments.CommentDto;
import ru.practicum.exception.CommenterAndInitiatorAreSameException;
import ru.practicum.exception.EventIsNotPublishedException;
import ru.practicum.exception.EventNotFoundException;
import ru.practicum.exception.UserNotFoundException;
import ru.practicum.model.CommentEntity;

public interface CommentsService {
    CommentDto create(Long commenterId, Long eventId, CommentEntity commentEntity) throws CommenterAndInitiatorAreSameException, EventIsNotPublishedException, UserNotFoundException, EventNotFoundException;

}
