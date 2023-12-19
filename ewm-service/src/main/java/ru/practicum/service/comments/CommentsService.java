package ru.practicum.service.comments;

import ru.practicum.dto.comments.CommentDto;
import ru.practicum.exception.*;
import ru.practicum.model.CommentEntity;

public interface CommentsService {
    CommentDto create(Long commenterId, Long eventId, CommentEntity commentEntity)
            throws UserNotFoundException,
            EventNotFoundException,
            EventIsNotPublishedException,
            CommenterAndInitiatorAreSameException;

    CommentDto findById(Long commenterId, Long commentId)
            throws UserNotFoundException,
            CommentNotFoundException,
            EventIsNotPublishedException,
            CommenterAndInitiatorAreSameException,
            CommentOwnerAndClaimToBeOwnerUserAreDifferentException;

    void deleteById(Long commenterId, Long commentId) throws UserNotFoundException, CommentNotFoundException, CommentOwnerAndClaimToBeOwnerUserAreDifferentException;
}
